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

    static void build(PsiClass psiClass, List result) {
        buildCreateQuery(psiClass.getProject(), psiClass, result);
        buildCreatePersistence(psiClass.getProject(), psiClass, result);
        buildCreate(psiClass.getProject(), psiClass, result);
        buildSave(psiClass.getProject(), psiClass, result);
        buildValidate(psiClass.getProject(), psiClass, result);
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

    private static void buildCreatePersistence(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "createPersistence");
        PsiType psiType = getProjectType("com.github.braisdom.objsql.Persistence", project);
        methodBuilder.withMethodReturnType(psiType)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL)
                .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

        result.add(methodBuilder);
    }

    private static void buildCreate(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "create");
        PsiType psiType = getProjectType(psiClass.getQualifiedName(), project);
        methodBuilder.withParameter("dirtyObject", getProjectType(psiClass.getQualifiedName(), project))
                .withParameter("skipValidation", PsiType.BOOLEAN)
                .withMethodReturnType(psiType)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL)
                .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

        result.add(methodBuilder);
    }

    private static void buildValidate(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "validate");
        PsiType psiType = getProjectType("com.github.braisdom.objsql.Validator.Violation", project);
        methodBuilder.withMethodReturnType(psiType.createArrayType())
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.FINAL);

        result.add(methodBuilder);
    }

    private static void buildSave(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "save");
        methodBuilder.withParameter("skipValidation", PsiType.BOOLEAN)
                .withMethodReturnType(PsiType.VOID)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.FINAL)
                .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

        result.add(methodBuilder);
    }
}
