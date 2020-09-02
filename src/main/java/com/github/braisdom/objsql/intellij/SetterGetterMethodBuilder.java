package com.github.braisdom.objsql.intellij;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.impl.light.LightMethodBuilder;

import java.util.Collection;
import java.util.List;

final class SetterGetterMethodBuilder {

    static void buildSetterGetterMethod(PsiClass psiClass, List result) {
        Collection<PsiField> fields =  PsiClassUtil.collectClassFieldsIntern(psiClass);
        for(PsiField field : fields) {
            String setterName = String.format("set%s", upperFirstChar(field.getName()));
            String getterName = String.format("%s%s", field.getType().equalsToText("Boolean") ? "is" : "get",
                    upperFirstChar(field.getName()));
            LightMethodBuilder setterMethodBuilder = new LightMethodBuilder(psiClass.getManager(), setterName);
            LightMethodBuilder getterMethodBuilder = new LightMethodBuilder(psiClass.getManager(), getterName);
        }
    }

    private static String upperFirstChar(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }
}
