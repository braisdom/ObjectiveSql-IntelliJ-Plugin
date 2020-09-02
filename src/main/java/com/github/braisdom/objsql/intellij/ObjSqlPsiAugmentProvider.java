package com.github.braisdom.objsql.intellij;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.augment.PsiAugmentProvider;
import com.intellij.psi.impl.source.PsiExtensibleClass;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ObjSqlPsiAugmentProvider extends PsiAugmentProvider {

    @NotNull
    @Override
    protected <Psi extends PsiElement> List<Psi> getAugments(@NotNull PsiElement element, @NotNull Class<Psi> type) {
        final List<Psi> result = new ArrayList<>();

        if ((type != PsiClass.class && type != PsiField.class && type != PsiMethod.class) || !(element instanceof PsiExtensibleClass)) {
            return result;
        }

        final PsiClass psiClass = (PsiClass) element;
        final Project project = element.getProject();

        PsiAnnotation psiAnnotation = PsiAnnotationSearchUtil.findAnnotation(psiClass,
                "com.github.braisdom.objsql.annotations.DomainModel");

        if (type == PsiMethod.class && psiAnnotation != null) {
            ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "createQuery");
            methodBuilder.withContainingClass((PsiClass) element)
                    .withMethodReturnType(PsiType.getTypeByName("com.github.braisdom.funcsql.Query", project,
                            GlobalSearchScope.allScope(project)))
                    .withNavigationElement(psiClass)
                    .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC);

            result.add((Psi) methodBuilder);
        }

        return result;
    }

}
