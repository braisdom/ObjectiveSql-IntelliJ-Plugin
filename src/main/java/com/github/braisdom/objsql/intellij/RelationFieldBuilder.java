package com.github.braisdom.objsql.intellij;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.light.LightFieldBuilder;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.List;

final class RelationFieldBuilder {

    static void build(PsiClass psiClass, List result) {
        Project project = psiClass.getProject();
        PsiType primaryType = PsiType.getTypeByName("java.lang.String", project, GlobalSearchScope.allScope(project));
        String primaryName = "DEMO";
        LightFieldBuilder primaryBuilder = new LightFieldBuilder(primaryName, primaryType, psiClass);
        primaryBuilder.setModifiers(PsiModifier.PUBLIC, PsiModifier.FINAL, PsiModifier.STATIC);
        primaryBuilder.setContainingClass(psiClass);

        result.add(primaryBuilder);
    }
}
