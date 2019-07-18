/**
 * MIT License
 * <p>
 * Copyright (c) 2017-2018 nuls.io
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.nuls.contract.rpc.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.googlecode.jsonrpc4j.ErrorData;
import com.googlecode.jsonrpc4j.ErrorResolver;
import io.nuls.core.exception.NulsRuntimeException;

import java.lang.reflect.Method;
import java.util.List;

import static com.googlecode.jsonrpc4j.ErrorResolver.JsonError.ERROR_NOT_HANDLED;

/**
 * @author: PierreLuo
 * @date: 2019-07-09
 */
public enum DefineErrorResolver implements ErrorResolver {
    INSTANCE;

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonError resolveError(Throwable throwable, Method method, List<JsonNode> arguments) {
        if(throwable.getClass().equals(NulsRuntimeException.class)){
            NulsRuntimeException exception=(NulsRuntimeException)throwable;
            return new JsonError(Integer.valueOf(exception.getCode()), throwable.getMessage(), null);
        }else{
            return new JsonError(ERROR_NOT_HANDLED.code, throwable.getMessage(), null);
        }
    }
}
