/* Copyright 2013 Artem Melentyev <amelentev@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.braisdom.objsql.intellij.oo;

import com.intellij.psi.PsiBinaryExpression;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.resolve.JavaResolveCache;
import com.intellij.psi.impl.source.tree.java.PsiBinaryExpressionImpl;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.TypeConversionUtil;
import com.intellij.util.Function;

public class PsiOOBinaryExpressionImpl extends PsiBinaryExpressionImpl {

    @Override
    public PsiType getType() {
        return JavaResolveCache.getInstance(getProject())
                .getType(this, (Function<PsiBinaryExpression, PsiType>) e -> {
            if (TypeConversionUtil.isBinaryOperatorApplicable(e.getOperationTokenType(),
                    e.getLOperand(), e.getROperand(), true)) {
                return doGetType((PsiBinaryExpressionImpl) e);
            }
            return OOResolver.getOOType(PsiOOBinaryExpressionImpl.this);
        });
    }

    private static PsiType doGetType(PsiBinaryExpressionImpl param) {
        PsiExpression lOperand = param.getLOperand();
        PsiExpression rOperand = param.getROperand();
        if (rOperand == null) return null;
        PsiType rType = rOperand.getType();
        IElementType sign = param.getOperationTokenType();
        // optimization: if we can calculate type based on right type only
        PsiType type = TypeConversionUtil.calcTypeForBinaryExpression(null, rType, sign, false);
        if (type != TypeConversionUtil.NULL_TYPE) return type;

        PsiType lType = lOperand.getType();
        return TypeConversionUtil.calcTypeForBinaryExpression(lType, rType, sign, true);
    }
}
