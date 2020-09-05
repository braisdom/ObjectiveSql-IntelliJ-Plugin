package com.github.braisdom.objsql.intellij;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.light.LightFieldBuilder;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.Collection;
import java.util.List;

final class RelationFieldBuilder {

    private static final String RELATION_ANNOTATION = "com.github.braisdom.objsql.annotations.Relation";

    static void buildField(PsiClass psiClass, List result) {
        final Project project = psiClass.getProject();
        Collection<PsiField> fields = PsiClassUtil.collectClassFieldsIntern(psiClass);
        for (PsiField field : fields) {
            PsiAnnotation annotation = field.getAnnotation(RELATION_ANNOTATION);
            if (annotation != null) {
                PsiType primaryType = PsiType.getTypeByName("com.github.braisdom.objsql.relation.Relationship", project, GlobalSearchScope.allScope(project));
                String fieldName = genFieldName(field, annotation);
                if(fieldName != null) {
                    LightFieldBuilder primaryBuilder = new LightFieldBuilder(fieldName, primaryType, psiClass);
                    primaryBuilder.setModifiers(PsiModifier.PUBLIC, PsiModifier.FINAL, PsiModifier.STATIC);
                    primaryBuilder.setContainingClass(psiClass);

                    result.add(primaryBuilder);
                }
            }
        }
    }

    static String genFieldName(PsiField psiField, PsiAnnotation annotationValue) {
        String value = annotationValue.findAttributeValue("relationType").getText();
        if ("RelationType.HAS_MANY".equalsIgnoreCase(value)) {
            return WordUtil.underscore(String.format("%s_%s", "HAS_MANY", psiField.getName())).toUpperCase();
        } else if ("RelationType.HAS_ONE".equalsIgnoreCase(value)) {
            return WordUtil.underscore(String.format("%s_%s", "HAS_ONE", psiField.getName())).toUpperCase();
        } else if ("RelationType.BELONGS_TO".equalsIgnoreCase(value)) {
            return WordUtil.underscore(String.format("%s_%s", "BELONGS_TO", psiField.getName())).toUpperCase();
        }
        return null;
    }
}
