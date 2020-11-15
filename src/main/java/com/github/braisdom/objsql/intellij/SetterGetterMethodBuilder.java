package com.github.braisdom.objsql.intellij;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.Collection;
import java.util.List;

import static com.github.braisdom.objsql.intellij.ObjSqlPsiAugmentProvider.checkMethodExists;

final class SetterGetterMethodBuilder {

    private static final Logger LOGGER = Logger.getInstance(SetterGetterMethodBuilder.class.getName());

    static void buildMethod(PsiClass psiClass, List result) {
        final Project project = psiClass.getProject();
        Collection<PsiField> fields = PsiClassUtil.collectClassFieldsIntern(psiClass);
        LOGGER.warn("Field count: " + fields.size());
        for (PsiField field : fields) {
            String setterName = String.format("set%s", upperFirstChar(field.getName()));
            String getterName = String.format("%s%s", isBoolean(field.getType()) ? "is" : "get",
                    upperFirstChar(field.getName()));
            ObjSqlLightMethodBuilder setterMethodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), setterName);
            ObjSqlLightMethodBuilder getterMethodBuilder = new ObjSqlLightMethodBuilder(psiClass.getManager(), getterName);

            setterMethodBuilder.withParameter(field.getName(), field.getType())
                    .withMethodReturnType(PsiType.getTypeByName(psiClass.getQualifiedName(),
                            project, GlobalSearchScope.allScope(project)))
                    .withContainingClass(psiClass)
                    .withModifier(PsiModifier.PUBLIC);
            if(!checkMethodExists(psiClass, setterMethodBuilder))
                result.add(setterMethodBuilder);

            getterMethodBuilder.withMethodReturnType(field.getType())
                    .withContainingClass(psiClass)
                    .withModifier(PsiModifier.PUBLIC);

            if(!checkMethodExists(psiClass, getterMethodBuilder))
                result.add(getterMethodBuilder);
        }
    }

    static boolean isBoolean(PsiType type) {
        String typeText = type.getCanonicalText();
        return typeText.equalsIgnoreCase("boolean") ||
                typeText.equalsIgnoreCase("java.lang.Boolean");
    }

    static String upperFirstChar(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }
}
