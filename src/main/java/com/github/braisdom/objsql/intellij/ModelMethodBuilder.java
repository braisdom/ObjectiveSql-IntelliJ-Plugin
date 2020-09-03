package com.github.braisdom.objsql.intellij;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.light.LightFieldBuilder;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.List;

import static com.github.braisdom.objsql.intellij.ObjSqlPsiAugmentProvider.createParameterInterfaceType;
import static com.github.braisdom.objsql.intellij.ObjSqlPsiAugmentProvider.getProjectType;

final class ModelMethodBuilder {

    static void buildField(PsiClass psiClass, List result) {
        buildTableName(psiClass, result);
        buildRawAttributes(psiClass, result);
    }

    static void buildMethod(PsiClass psiClass, List result) {
        buildGetRawAttributes(psiClass, result);
        buildGetRawAttribute(psiClass, result);
    }

    private static void buildRawAttributes(PsiClass psiClass, List result) {
        PsiType fieldType = createParameterInterfaceType(psiClass.getProject(),
                "java.util.Map", "java.lang.String", "java.lang.Object");
        LightFieldBuilder primaryBuilder = new LightFieldBuilder("rawAttributes", fieldType, psiClass);
        primaryBuilder.setModifiers(PsiModifier.PRIVATE, PsiModifier.FINAL);
        primaryBuilder.setContainingClass(psiClass);

        result.add(primaryBuilder);
    }

    private static void buildTableName(PsiClass psiClass, List result) {
        PsiType fieldType = createParameterInterfaceType(psiClass.getProject(),
                "java.util.Map", "java.lang.String", "java.lang.Object");
        LightFieldBuilder primaryBuilder = new LightFieldBuilder("TABLE_NAME", fieldType, psiClass);
        primaryBuilder.setModifiers(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL);
        primaryBuilder.setContainingClass(psiClass);

        result.add(primaryBuilder);
    }

    private static void buildGetRawAttributes(PsiClass psiClass, List result) {
        Project project = psiClass.getProject();
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "getRawAttributes");
        PsiType psiType = getProjectType("java.util.Map", project);
        methodBuilder.withMethodReturnType(psiType)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.FINAL);

        result.add(methodBuilder);
    }

    private static void buildGetRawAttribute(PsiClass psiClass, List result) {
        Project project = psiClass.getProject();
        ObjSqlLightMethodBuilder methodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "getRawAttribute");
        PsiType psiType = getProjectType("java.lang.Object", project);
        methodBuilder.withParameter("key", "java.lang.String")
                .withMethodReturnType(psiType)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.FINAL);

        result.add(methodBuilder);
    }
}
