package com.github.braisdom.objsql.intellij;

import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.light.LightPsiClassBuilder;
import org.jetbrains.annotations.NotNull;

public class ObjSqlLightClassBuilder extends LightPsiClassBuilder {
  private final String myQualifiedName;

  public ObjSqlLightClassBuilder(@NotNull PsiElement context,
                                 @NotNull String simpleName,
                                 @NotNull String qualifiedName) {
    super(context, simpleName);
    myQualifiedName = qualifiedName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ObjSqlLightClassBuilder that = (ObjSqlLightClassBuilder) o;

    return myQualifiedName.equals(that.myQualifiedName);
  }

  @Override
  public int hashCode() {
    return myQualifiedName.hashCode();
  }
}
