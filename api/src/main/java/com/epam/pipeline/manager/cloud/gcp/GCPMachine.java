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

package com.epam.pipeline.manager.cloud.gcp;

import lombok.Value;

@Value
public class GCPMachine {
    private final String name;
    private final String family;
    private final int cpu;
    private final double ram;
    private final int gpu;
    private final String gpuType;

    static GCPMachine withCpu(final String name, final String family, final int cpu, final double ram) {
        return new GCPMachine(name, family, cpu, ram, 0, null);
    }

    static GCPMachine withGpu(final String name, final String family, final int cpu, final double ram,
                              final int gpu, final String gpuType) {
        return new GCPMachine(name, family, cpu, ram, gpu, gpuType);
    }
}