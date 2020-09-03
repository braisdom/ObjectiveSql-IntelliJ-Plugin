package com.github.braisdom.objsql.intellij;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.List;

import static com.github.braisdom.objsql.intellij.PsiClassUtil.getProjectType;

final class QueryMethodBuilder {

    static void buildMethod(PsiClass psiClass, List result) {
        buildCreateQuery(psiClass.getProject(), psiClass, result);
    }

    private static void buildCreateQuery(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "createQuery");
        PsiType psiType = getProjectType("com.github.braisdom.objsql.Query", project);
        methodBuilder.withMethodReturnType(psiType)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL)
                .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

        result.add(methodBuilder);
    }
}
