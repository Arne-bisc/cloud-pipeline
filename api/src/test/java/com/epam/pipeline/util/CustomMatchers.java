/*
 * Copyright 2017-2019 EPAM Systems, Inc. (https://www.epam.com/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.epam.pipeline.util;

import java.util.Collection;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public final class CustomMatchers {

    private CustomMatchers() {}

    public static <T> Matcher<Collection<T>> isEmpty() {
        return new BaseMatcher<Collection<T>>() {
            @Override
            public boolean matches(final Object item) {
                return item instanceof Collection
                        && ((Collection) item).isEmpty();
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("collection is empty");
            }
        };
    }
}
