package com.github.braisdom.objsql.intellij;

import com.intellij.debugger.SourcePosition;
import com.intellij.debugger.engine.PositionManagerImpl;
import com.intellij.debugger.engine.SourcePositionProvider;
import com.intellij.debugger.impl.DebuggerContextImpl;
import com.intellij.debugger.ui.tree.NodeDescriptor;
import com.intellij.openapi.compiler.CompilerPaths;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.PlatformVirtualFileManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ObjSqlSourcePositionProvider extends SourcePositionProvider {
    @Override
    protected @Nullable SourcePosition computeSourcePosition(@NotNull NodeDescriptor descriptor,
                                                             @NotNull Project project,
                                                             @NotNull DebuggerContextImpl context,
                                                             boolean nearest) {
        return new ObjSqlSourcePosition(project, context.getSourcePosition(), 1);
    }

    private class ObjSqlSourcePosition extends PositionManagerImpl.JavaSourcePosition {

        private final Project project;

        public ObjSqlSourcePosition(Project project, SourcePosition delegate, int lambdaOrdinal) {
            super(delegate, lambdaOrdinal);
            this.project = project;
        }

        @Override
        public void navigate(boolean requestFocus) {
            super.navigate(requestFocus);
        }

        @Override
        public boolean canNavigateToSource() {
            return super.canNavigateToSource();
        }

        @Override
        public SourcePosition mapDelegate(SourcePosition original) {
            return super.mapDelegate(original);
        }

        @Override
        public Editor openEditor(boolean requestFocus) {
            return super.openEditor(requestFocus);
        }

        @Override
        public @NotNull PsiFile getFile() {
            String[] outputPaths = CompilerPaths.getOutputPaths(ModuleManager.getInstance(project).getModules());
            String classFileName = "Demo.class";
            String fullClassFileName = String.format("file://%s/%s", outputPaths[0], classFileName);
            VirtualFile virtualFile = PlatformVirtualFileManager.getInstance().findFileByUrl(fullClassFileName);
            PsiJavaFile psiJavaFile = (PsiJavaFile) PsiUtil.getPsiFile(project, virtualFile);
            return psiJavaFile;
        }

        @Override
        public PsiElement getElementAt() {
            return super.getElementAt();
        }

        @Override
        public int getLine() {
            return super.getLine();
        }

        @Override
        public boolean canNavigate() {
            return super.canNavigate();
        }
    }
}
