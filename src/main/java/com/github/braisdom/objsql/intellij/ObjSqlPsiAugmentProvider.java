package com.github.braisdom.objsql.intellij;

import com.intellij.openapi.util.RecursionGuard;
import com.intellij.openapi.util.RecursionManager;
import com.intellij.psi.*;
import com.intellij.psi.augment.PsiAugmentProvider;
import com.intellij.psi.impl.source.PsiExtensibleClass;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ObjSqlPsiAugmentProvider extends PsiAugmentProvider {

    public static final String DOMAIN_MODEL_CLASSNAME = "com.github.braisdom.objsql.annotations.DomainModel";

    @NotNull
    @Override
    protected <Psi extends PsiElement> List<Psi> getAugments(@NotNull PsiElement element, @NotNull Class<Psi> type) {
        final List<Psi> result = new ArrayList<>();

        if ((type != PsiClass.class && type != PsiField.class && type != PsiMethod.class) || !(element instanceof PsiExtensibleClass)) {
            return result;
        }

        final PsiClass psiClass = (PsiClass) element;
        if (psiClass.isAnnotationType() || psiClass.isInterface()) {
            return result;
        }

        PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil.findAnnotation(psiClass, DOMAIN_MODEL_CLASSNAME);

        if (psiAnnotation == null)
            return result;

        final List<Psi> cachedValue;
        if (type == PsiMethod.class)
            cachedValue = CachedValuesManager.getCachedValue(element, new MethodCachedValueProvider<>(type, psiClass));
        else if(type == PsiField.class)
            cachedValue = CachedValuesManager.getCachedValue(element, new FieldCachedValueProvider<>(type, psiClass));
        else return result;

        return null != cachedValue ? cachedValue : result;
    }

    private static class FieldCachedValueProvider<Psi extends PsiElement> extends ObjSqlCachedValueProvider<Psi> {
        private static final RecursionGuard ourGuard = RecursionManager.createGuard("objsql.augment.field");

        FieldCachedValueProvider(Class<Psi> type, PsiClass psiClass) {
            super(type, psiClass, ourGuard);
        }
    }

    private static class MethodCachedValueProvider<Psi extends PsiElement> extends ObjSqlCachedValueProvider<Psi> {
        private static final RecursionGuard ourGuard = RecursionManager.createGuard("objsql.augment.method");

        MethodCachedValueProvider(Class<Psi> type, PsiClass psiClass) {
            super(type, psiClass, ourGuard);
        }
    }

    private static class ClassCachedValueProvider<Psi extends PsiElement> extends ObjSqlCachedValueProvider<Psi> {
        private static final RecursionGuard ourGuard = RecursionManager.createGuard("objsql.augment.class");

        ClassCachedValueProvider(Class<Psi> type, PsiClass psiClass) {
            super(type, psiClass, ourGuard);
        }
    }

    private abstract static class ObjSqlCachedValueProvider<Psi extends PsiElement> implements CachedValueProvider<List<Psi>> {
        private final Class<Psi> type;
        private final PsiClass psiClass;
        private final RecursionGuard recursionGuard;

        ObjSqlCachedValueProvider(Class<Psi> type, PsiClass psiClass, RecursionGuard recursionGuard) {
            this.type = type;
            this.psiClass = psiClass;
            this.recursionGuard = recursionGuard;
        }

        @Override
        public Result<List<Psi>> compute() {
            return recursionGuard.doPreventingRecursion(psiClass, true, () -> {
                final List<Psi> result = new ArrayList<>();

                if (type == PsiMethod.class) {
                    SetterGetterMethodBuilder.build(psiClass, result);
                    PrimaryBuilder.build(psiClass, result);
                    QueryMethodBuilder.build(psiClass, result);
                } else if(type == PsiField.class) {
                    RelationFieldBuilder.build(psiClass, result);
                    PrimaryBuilder.buildPrimaryField(psiClass, result);
                }

                return Result.create(result, PsiModificationTracker.JAVA_STRUCTURE_MODIFICATION_COUNT);
            });
        }
    }

}
