package com.github.braisdom.objsql.intellij;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.light.LightFieldBuilder;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.Arrays;
import java.util.List;

import static com.github.braisdom.objsql.intellij.ObjSqlPsiAugmentProvider.DOMAIN_MODEL_CLASSNAME;
import static com.github.braisdom.objsql.intellij.SetterGetterMethodBuilder.upperFirstChar;

final class PrimaryBuilder {

    static void buildPrimaryField(PsiClass psiClass, List result) {
        PsiType primaryType = getPrimaryType(psiClass);
        String primaryName = getPrimaryName(psiClass);
        LightFieldBuilder primaryBuilder = new LightFieldBuilder(psiClass.getManager(), primaryName, primaryType);
        primaryBuilder.setModifiers(PsiModifier.PRIVATE);
        primaryBuilder.setNavigationElement(psiClass);

        PsiElementFactory psiElementFactory = JavaPsiFacade.getElementFactory(psiClass.getProject());
        PsiExpression initializer = psiElementFactory.createExpressionFromText("\"" + primaryName + "\"", psiClass);
        primaryBuilder.setInitializer(initializer);

        result.add(result);
    }

    static void buildPrimarySG(PsiClass psiClass, List result) {
        Project project = psiClass.getProject();
        PsiType primaryType = getPrimaryType(psiClass);
        String primaryName = getPrimaryName(psiClass);

        String setterName = String.format("set%s", upperFirstChar(primaryName));
        String getterName = String.format("get%s", upperFirstChar(primaryName));

        ObjSqlLightMethodBuilder setterMethodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), setterName);
        ObjSqlLightMethodBuilder getterMethodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), getterName);

        setterMethodBuilder.withParameter(primaryName, primaryType)
                .withMethodReturnType(PsiType.getTypeByName(psiClass.getQualifiedName(),
                        project, GlobalSearchScope.allScope(project)))
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC);

        getterMethodBuilder.withMethodReturnType(primaryType)
                .withContainingClass(psiClass)
                .withModifier(PsiModifier.PUBLIC);

        result.add(setterMethodBuilder);
        result.add(getterMethodBuilder);
    }

    static String getPrimaryName(PsiClass psiClass) {
        PsiAnnotationMemberValue annotationMemberValue = psiClass
                .getAnnotation(DOMAIN_MODEL_CLASSNAME).findAttributeValue("primaryFieldName");
        if(annotationMemberValue == null)
            return "id";
        else
            return annotationMemberValue.getText().replaceAll("^\"|\"$", "");
    }

    static PsiType getPrimaryType(PsiClass psiClass) {
        Project project = psiClass.getProject();
        String rawPrimaryTypeName = "Integer.class";
        PsiAnnotationMemberValue annotationMemberValue = psiClass
                .getAnnotation(DOMAIN_MODEL_CLASSNAME).findAttributeValue("primaryClass");
        if(annotationMemberValue != null)
            rawPrimaryTypeName = annotationMemberValue.getText();
        String[] rawPrimaryTypePart = rawPrimaryTypeName.split("\\.");
        String primaryTypeName = String.join(".",
                Arrays.copyOfRange(rawPrimaryTypePart, 0, rawPrimaryTypePart.length - 1));

        return PsiType.getTypeByName(primaryTypeName, project, GlobalSearchScope.allScope(project));
    }
}
