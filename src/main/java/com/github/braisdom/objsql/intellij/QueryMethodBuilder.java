package com.github.braisdom.objsql.intellij;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.List;

final class QueryMethodBuilder {

    static void build(PsiClass psiClass, List result) {
        buildCreateQuery(psiClass.getProject(), psiClass, result);
    }

    private static void buildCreateQuery(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder setterMethodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "createQuery");
        PsiType psiType = PsiType.getTypeByName("com.github.braisdom.objsql.Query", project, GlobalSearchScope.allScope(project));
        setterMethodBuilder.withMethodReturnType(psiType)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL)
                .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

        result.add(setterMethodBuilder);
    }
}
