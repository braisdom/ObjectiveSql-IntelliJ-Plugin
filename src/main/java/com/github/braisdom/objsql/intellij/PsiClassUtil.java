package com.github.braisdom.objsql.intellij;

import com.intellij.openapi.compiler.ex.CompilerPathsEx;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.PlatformVirtualFileManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.impl.source.PsiExtensibleClass;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Plushnikov Michail
 */
public class PsiClassUtil {

  @FunctionalInterface
  public interface NavigateAction {

    boolean apply(PsiClass[] psiClasses);
  }

  /**
   * Workaround to get all of original Fields of the psiClass, without calling PsiAugmentProvider infinitely
   *
   * @param psiClass psiClass to collect all of fields from
   * @return all intern fields of the class
   */
  @NotNull
  public static Collection<PsiField> collectClassFieldsIntern(@NotNull PsiClass psiClass) {
    if (psiClass instanceof PsiExtensibleClass) {
      return ((PsiExtensibleClass) psiClass).getOwnFields();
    } else {
      return filterPsiElements(psiClass, PsiField.class);
    }
  }

  private static <T extends PsiElement> Collection<T> filterPsiElements(@NotNull PsiClass psiClass, @NotNull Class<T> desiredClass) {
    return Arrays.stream(psiClass.getChildren()).filter(desiredClass::isInstance).map(desiredClass::cast).collect(Collectors.toList());
  }

  public static void navigate(Project project, PsiClass containingClass, NavigateAction action) {
    String qualifiedName = containingClass.getQualifiedName();
    String[] outputPaths = CompilerPathsEx.getOutputPaths(ModuleManager.getInstance(project).getModules());

    String classFileName = qualifiedName.replaceAll("\\.", "/") + ".class";

    for(String outputPath : outputPaths) {
      String fullClassFileName = String.format("file://%s/%s", outputPath, classFileName);
      VirtualFile virtualFile = PlatformVirtualFileManager.getInstance().findFileByUrl(fullClassFileName);
      if(virtualFile != null) {
        PsiJavaFile psiJavaFile = (PsiJavaFile) PsiUtil.getPsiFile(project, virtualFile);
        PsiClass[] psiClasses = psiJavaFile.getClasses();
        if(action.apply(psiClasses))
          return;
      }
    }

    String message = String.format("Cannot find '%s', \nyou can rebuild project and retry.", classFileName);
    Messages.showErrorDialog(project, message, "Error");
  }
}
