package com.github.braisdom.objsql.intellij;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.List;

import static com.github.braisdom.objsql.intellij.ObjSqlPsiAugmentProvider.*;

final class PersistenceMethodBuilder {

    static void buildMethod(PsiClass psiClass, List result) {
        buildCreatePersistence(psiClass.getProject(), psiClass, result);
        buildCreate(psiClass.getProject(), psiClass, result);
        buildCreateArray(psiClass.getProject(), psiClass, result);
        buildUpdate(psiClass.getProject(), psiClass, result);
        buildUpdate2(psiClass.getProject(), psiClass, result);
        buildDestroy(psiClass.getProject(), psiClass, result);
        buildDestroy2(psiClass.getProject(), psiClass, result);
        buildSave(psiClass.getProject(), psiClass, result);
        buildValidate(psiClass.getProject(), psiClass, result);
        buildExecute(psiClass.getProject(), psiClass, result);
        buildNewInstanceFrom(psiClass.getProject(), psiClass, result);
        buildNewInstance3From(psiClass.getProject(), psiClass, result);
    }

    private static void buildCreatePersistence(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(),
                "createPersistence");
        PsiType psiType = getProjectType("com.github.braisdom.objsql.Persistence", project);
        methodBuilder.withMethodReturnType(psiType)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL)
                .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

        if(!checkMethodExists(psiClass, methodBuilder))
            result.add(methodBuilder);
    }

    private static void buildCreate(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder createBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "create");
        PsiType psiType = getProjectType(psiClass.getQualifiedName(), project);
        createBuilder.withParameter("dirtyObject", getProjectType(psiClass.getQualifiedName(), project))
                .withParameter("skipValidation", PsiType.BOOLEAN)
                .withMethodReturnType(psiType)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL)
                .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

        if(!checkMethodExists(psiClass, createBuilder))
            result.add(createBuilder);
    }

    private static void buildCreateArray(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "create");
        methodBuilder.withParameter("dirtyObjects", getProjectType(psiClass.getQualifiedName(), project).createArrayType())
                .withParameter("skipValidation", PsiType.BOOLEAN)
                .withMethodReturnType(PsiType.INT.createArrayType())
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL)
                .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

        if(!checkMethodExists(psiClass, methodBuilder))
            result.add(methodBuilder);
    }

    private static void buildValidate(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "validate");
        PsiType psiType = getProjectType("com.github.braisdom.objsql.Validator.Violation", project);
        methodBuilder.withMethodReturnType(psiType.createArrayType())
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.FINAL);

        if(!checkMethodExists(psiClass, methodBuilder))
            result.add(methodBuilder);
    }

    private static void buildSave(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "save");
        methodBuilder.withParameter("skipValidation", PsiType.BOOLEAN)
                .withMethodReturnType(getProjectType(psiClass.getQualifiedName(), project))
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.FINAL)
                .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

        if(!checkMethodExists(psiClass, methodBuilder))
            result.add(methodBuilder);
    }

    private static void buildUpdate(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "update");
        PsiType primaryType = getPrimaryType(psiClass);
        methodBuilder
                .withParameter("id", primaryType)
                .withParameter("dirtyObject", getProjectType(psiClass.getQualifiedName(), project))
                .withParameter("skipValidation", PsiType.BOOLEAN)
                .withMethodReturnType(getProjectType(psiClass.getQualifiedName(), project))
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL)
                .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

        if(!checkMethodExists(psiClass, methodBuilder))
            result.add(methodBuilder);
    }

    private static void buildUpdate2(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "update");
        methodBuilder
                .withParameter("updates", getProjectType("java.lang.String", project))
                .withParameter("predicates", getProjectType("java.lang.String", project))
                .withParameter("args", "java.lang.Object", true)
                .withMethodReturnType(PsiType.INT)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL)
                .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

        if(!checkMethodExists(psiClass, methodBuilder))
            result.add(methodBuilder);
    }

    private static void buildDestroy(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "destroy");
        PsiType primaryType = getPrimaryType(psiClass);
        methodBuilder
                .withParameter("id", primaryType)
                .withMethodReturnType(PsiType.INT)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL)
                .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

        if(!checkMethodExists(psiClass, methodBuilder))
            result.add(methodBuilder);
    }

    private static void buildDestroy2(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "destroy");
        methodBuilder
                .withParameter("predicates", getProjectType("java.lang.String", project))
                .withParameter("args", "java.lang.Object", true)
                .withMethodReturnType(PsiType.INT)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL)
                .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

        if(!checkMethodExists(psiClass, methodBuilder))
            result.add(methodBuilder);
    }

    private static void buildExecute(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "execute");
        methodBuilder
                .withParameter("sql", getProjectType("java.lang.String", project))
                .withParameter("args", getProjectType("java.lang.Object", project), true)
                .withMethodReturnType(PsiType.INT)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL)
                .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

        if(!checkMethodExists(psiClass, methodBuilder))
            result.add(methodBuilder);
    }

    private static void buildNewInstanceFrom(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "newInstanceFrom");
        methodBuilder
                .withParameter("properties", getProjectType("java.util.Map", project))
                .withParameter("underLine", PsiType.BOOLEAN)
                .withMethodReturnType(getProjectType(psiClass.getQualifiedName(), project))
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL);

        if(!checkMethodExists(psiClass, methodBuilder))
            result.add(methodBuilder);
    }

    private static void buildNewInstance3From(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "newInstanceFrom");
        methodBuilder
                .withParameter("properties", getProjectType("java.util.Map", project))
                .withMethodReturnType(getProjectType(psiClass.getQualifiedName(), project))
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL);

        if(!checkMethodExists(psiClass, methodBuilder))
            result.add(methodBuilder);
    }
}
