package com.github.braisdom.objsql.intellij;

import com.intellij.ide.util.PsiNavigationSupport;
import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.intellij.psi.impl.light.LightFieldBuilder;
import org.jetbrains.annotations.NotNull;

public class ObjsqlLightFieldBuilder extends LightFieldBuilder {

    public ObjsqlLightFieldBuilder(@NotNull String name, @NotNull PsiType type, @NotNull PsiElement navigationElement) {
        super(name, type, navigationElement);
    }

    @Override
    public void navigate(boolean requestFocus) {
        PsiClassUtil.navigate(getProject(), getContainingClass(), (psiClasses -> {
            for (PsiClass psiClass : psiClasses) {
                PsiField[] psiFields = psiClass.getFields();
                for (PsiField psiField : psiFields) {
                    if (getName().equals(psiField.getName())) {
                        Navigatable descriptor = PsiNavigationSupport.getInstance().getDescriptor(psiField);
                        if (descriptor != null) {
                            descriptor.navigate(requestFocus);
                            return true;
                        }
                    }
                }
            }
            return false;
        }));
    }

    @Override
    public boolean canNavigate() {
        return PsiNavigationSupport.getInstance().canNavigate(this);
    }

    @Override
    public boolean canNavigateToSource() {
        return canNavigate();
    }
}
