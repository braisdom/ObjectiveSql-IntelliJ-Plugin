package com.github.braisdom.objsql.intellij;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.Collection;
import java.util.List;

import static com.github.braisdom.objsql.intellij.ObjSqlPsiAugmentProvider.createParameterType;
import static com.github.braisdom.objsql.intellij.ObjSqlPsiAugmentProvider.getProjectType;

final class QueryMethodBuilder {

    static void buildMethod(PsiClass psiClass, List result) {
        buildCreateQuery(psiClass.getProject(), psiClass, result);
        buildCount(psiClass.getProject(), psiClass, result);
        buildQuery(psiClass.getProject(), psiClass, result);
        buildQuery2(psiClass.getProject(), psiClass, result);
        buildQuery3(psiClass.getProject(), psiClass, result);
        buildQueryFirst(psiClass.getProject(), psiClass, result);
        buildQueryFirst2(psiClass.getProject(), psiClass, result);
        buildQueryAll(psiClass.getProject(), psiClass, result);
        buildQueryableField(psiClass.getProject(), psiClass, result);
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

    private static void buildCount(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "count");
        methodBuilder.withParameter("predicate", "java.lang.String")
                .withParameter("args", "java.lang.Object", true)
                .withMethodReturnType(PsiType.INT)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL)
                .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

        result.add(methodBuilder);
    }

    private static void buildQuery(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "query");
        PsiType returnType = createParameterType(project, "java.util.List", psiClass.getQualifiedName());
        methodBuilder.withParameter("predicate", "java.lang.String")
                .withParameter("args", "java.lang.Object", true)
                .withMethodReturnType(returnType)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL)
                .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

        result.add(methodBuilder);
    }

    private static void buildQuery2(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "query");
        PsiType returnType = createParameterType(project, "java.util.List", psiClass.getQualifiedName());
        methodBuilder.withParameter("predicate", "java.lang.String")
                .withParameter("relations",
                        getProjectType("com.github.braisdom.objsql.relation.Relationship", project).createArrayType())
                .withParameter("args", "java.lang.Object", true)
                .withMethodReturnType(returnType)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL)
                .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

        result.add(methodBuilder);
    }

    private static void buildQuery3(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "queryBySql");
        PsiType returnType = createParameterType(project, "java.util.List", psiClass.getQualifiedName());
        methodBuilder.withParameter("predicate", "java.lang.String")
                .withParameter("args", "java.lang.Object", true)
                .withMethodReturnType(returnType)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL)
                .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

        result.add(methodBuilder);
    }

    private static void buildQueryAll(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "queryAll");
        PsiType returnType = createParameterType(project, "java.util.List", psiClass.getQualifiedName());
        methodBuilder.withParameter("relations", "com.github.braisdom.objsql.relation.Relationship", true)
                .withMethodReturnType(returnType)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL)
                .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

        result.add(methodBuilder);
    }

    private static void buildQueryFirst(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "queryFirst");
        PsiType returnType = getProjectType(psiClass.getQualifiedName(), project);
        methodBuilder.withParameter("predicate", "java.lang.String")
                .withParameter("relations",
                        getProjectType("com.github.braisdom.objsql.relation.Relationship", project).createArrayType())
                .withParameter("args", "java.lang.Object", true)
                .withMethodReturnType(returnType)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL)
                .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

        result.add(methodBuilder);
    }

    private static void buildQueryFirst2(Project project, PsiClass psiClass, List result) {
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "queryFirst");
        PsiType returnType = getProjectType(psiClass.getQualifiedName(), project);
        methodBuilder.withParameter("predicate", "java.lang.String")
                .withParameter("args", "java.lang.Object", true)
                .withMethodReturnType(returnType)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL)
                .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

        result.add(methodBuilder);
    }

    private static void buildQueryableField(Project project, PsiClass psiClass, List result) {
        Collection<PsiField> fields =  PsiClassUtil.collectClassFieldsIntern(psiClass);
        for(PsiField field : fields) {
            PsiAnnotation annotation = field
                    .getAnnotation("com.github.braisdom.objsql.annotations.Queryable");
            if(annotation != null) {
                String methodName = WordUtil.camelize(String.format("%s_%s", "queryBy", field.getName()), true);
                ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), methodName);
                PsiType returnType = createParameterType(project, "java.util.List", psiClass.getQualifiedName());
                methodBuilder.withParameter("value", field.getType())
                        .withMethodReturnType(returnType)
                        .withContainingClass(psiClass)
                        .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL)
                        .withException(PsiClassType.getTypeByName("java.sql.SQLException", project, GlobalSearchScope.allScope(project)));

                result.add(methodBuilder);
            }
        }
    }
}
