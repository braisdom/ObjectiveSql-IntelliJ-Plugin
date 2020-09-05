package com.github.braisdom.objsql.intellij;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.RecursionGuard;
import com.intellij.openapi.util.RecursionManager;
import com.intellij.psi.*;
import com.intellij.psi.augment.PsiAugmentProvider;
import com.intellij.psi.impl.source.PsiExtensibleClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
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
        else if (type == PsiField.class)
            cachedValue = CachedValuesManager.getCachedValue(element, new FieldCachedValueProvider<>(type, psiClass));
        else if (type == PsiClass.class)
            cachedValue = CachedValuesManager.getCachedValue(element, new ClassCachedValueProvider<>(type, psiClass));
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
                    SetterGetterMethodBuilder.buildMethod(psiClass, result);
                    PrimaryBuilder.buildMethod(psiClass, result);
                    QueryMethodBuilder.buildMethod(psiClass, result);
                    PersistenceMethodBuilder.buildMethod(psiClass, result);
                    ModelMethodBuilder.buildMethod(psiClass, result);
                    TableClassBuilder.buildMethod(psiClass, result);
                } else if (type == PsiField.class) {
                    RelationFieldBuilder.buildField(psiClass, result);
                    PrimaryBuilder.buildField(psiClass, result);
                    ModelMethodBuilder.buildField(psiClass, result);
                } else if (type == PsiClass.class) {
                    TableClassBuilder.buildClass(psiClass, result);
                }

                return Result.create(result, PsiModificationTracker.JAVA_STRUCTURE_MODIFICATION_COUNT);
            });
        }
    }

    static String getPrimaryName(PsiClass psiClass) {
        PsiAnnotation annotation = psiClass.getAnnotation(DOMAIN_MODEL_CLASSNAME);
        if (annotation == null)
            return "id";
        else {
            PsiAnnotationMemberValue annotationMemberValue = annotation.findAttributeValue("primaryFieldName");
            if(annotationMemberValue != null)
                return annotationMemberValue.getText().replaceAll("^\"|\"$", "");
            else return null;
        }
    }

    static PsiType getPrimaryType(PsiClass psiClass) {
        Project project = psiClass.getProject();
        String rawPrimaryTypeName = "Integer";
        PsiAnnotation annotation = psiClass.getAnnotation(DOMAIN_MODEL_CLASSNAME);
        if (annotation != null) {
            PsiAnnotationMemberValue annotationMemberValue = annotation.findAttributeValue("primaryClass");
            if (annotationMemberValue != null)
                rawPrimaryTypeName = annotationMemberValue.getText();
            String[] rawPrimaryTypePart = rawPrimaryTypeName.split("\\.");
            String primaryTypeName = String.join(".",
                    Arrays.copyOfRange(rawPrimaryTypePart, 0, rawPrimaryTypePart.length - 1));
            if (!primaryTypeName.startsWith("java.lang"))
                primaryTypeName = String.format("java.lang.%s", primaryTypeName);

            return PsiType.getTypeByName(primaryTypeName, project, GlobalSearchScope.allScope(project));
        } else
            return PsiType.getTypeByName("java.lang.Integer", project, GlobalSearchScope.allScope(project));
    }

    static PsiType getProjectType(String qName, Project project) {
        return PsiType.getTypeByName(qName,
                project, GlobalSearchScope.allScope(project));
    }

    static PsiType createParameterType(Project project, String qName, String... parameters) {
        return createParameterType(project, (PsiClassType) getProjectType(qName, project), parameters);
    }

    static PsiType createParameterType(Project project, PsiClassType classType, String... parameters) {
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);
        PsiClass psiClass = classType.resolve();
        if (psiClass.getTypeParameters().length == parameters.length) {
            List<PsiType> psiTypes = new ArrayList<>();
            for (String parameter : parameters)
                psiTypes.add(getProjectType(parameter, project));
            return factory.createType(classType.resolve(), psiTypes.toArray(new PsiType[]{}));
        } else return null;
    }

}
