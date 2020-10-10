package com.github.braisdom.objsql.intellij;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.light.LightFieldBuilder;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.List;

import static com.github.braisdom.objsql.intellij.ObjSqlPsiAugmentProvider.getPrimaryName;
import static com.github.braisdom.objsql.intellij.ObjSqlPsiAugmentProvider.getPrimaryType;
import static com.github.braisdom.objsql.intellij.SetterGetterMethodBuilder.upperFirstChar;

final class PrimaryBuilder {

    static void buildField(PsiClass psiClass, List result) {
        PsiType primaryType = getPrimaryType(psiClass);
        String primaryName = getPrimaryName(psiClass);
        ObjsqlLightFieldBuilder primaryBuilder = new ObjsqlLightFieldBuilder(primaryName, primaryType, psiClass);
        primaryBuilder.setModifiers(PsiModifier.PRIVATE);
        primaryBuilder.setContainingClass(psiClass);

        result.add(primaryBuilder);
    }

    static void buildMethod(PsiClass psiClass, List result) {
        Project project = psiClass.getProject();
        PsiType primaryType = getPrimaryType(psiClass);
        String primaryName = getPrimaryName(psiClass);

        String setterName = String.format("set%s", upperFirstChar(primaryName));
        String getterName = String.format("get%s", upperFirstChar(primaryName));

        ObjSqlLightMethodBuilder setterMethodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), setterName);
        ObjSqlLightMethodBuilder getterMethodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), getterName);
        ObjSqlLightMethodBuilder queryByPrimaryBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), "queryByPrimaryKey");

        setterMethodBuilder.withParameter(primaryName, primaryType)
                .withMethodReturnType(PsiType.getTypeByName(psiClass.getQualifiedName(),
                        project, GlobalSearchScope.allScope(project)))
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC);

        queryByPrimaryBuilder.withParameter(primaryName, primaryType)
                .withParameter("relations", PsiType.getTypeByName(
                        "com.github.braisdom.objsql.relation.Relationship",
                        project, GlobalSearchScope.allScope(project)), true)
                .withMethodReturnType(PsiType.getTypeByName(psiClass.getQualifiedName(),
                        project, GlobalSearchScope.allScope(project)))
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC, PsiModifier.STATIC, PsiModifier.FINAL);

        getterMethodBuilder.withMethodReturnType(primaryType)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC);

        result.add(setterMethodBuilder);
        result.add(getterMethodBuilder);
        result.add(queryByPrimaryBuilder);
    }
}
