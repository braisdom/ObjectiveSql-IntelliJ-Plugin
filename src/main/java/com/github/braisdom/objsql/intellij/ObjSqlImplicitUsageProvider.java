package com.github.braisdom.objsql.intellij;

import com.intellij.codeInsight.daemon.ImplicitUsageProvider;
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
        if (element instanceof PsiField) {
            PsiClass psiClass = ((PsiField)element).getContainingClass();
            return psiClass.hasAnnotation(ObjSqlPsiAugmentProvider.DOMAIN_MODEL_CLASSNAME);
        }
        return false;
    }

}