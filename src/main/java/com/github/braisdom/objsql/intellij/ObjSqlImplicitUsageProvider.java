package com.github.braisdom.objsql.intellij;

import com.intellij.codeInsight.daemon.ImplicitUsageProvider;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;

public class ObjSqlImplicitUsageProvider implements ImplicitUsageProvider {

    @Override
    public boolean isImplicitUsage(PsiElement element) {
        return isImplicitWrite(element) || isImplicitRead(element);
    }

    @Override
    public boolean isImplicitRead(PsiElement element) {
        return checkUsage(element);
    }

    @Override
    public boolean isImplicitWrite(PsiElement element) {
        return checkUsage(element);
    }

    private boolean checkUsage(PsiElement element) {
        boolean result = false;
        if (element instanceof PsiField) {
            PsiClass psiClass = ((PsiField)element).getContainingClass();
            PsiAnnotation annotation = psiClass.getAnnotation(ObjSqlPsiAugmentProvider.DOMAIN_MODEL_CLASSNAME);
            return annotation != null;
        }
        return false;
    }

}