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

package com.epam.pipeline.cmd;

import java.io.File;
import java.util.Collections;
import java.util.Map;

public interface CmdExecutor {

    /**
     * Executes the given command synchronously. Method <b>blocks</b> the current thread until the execution finishes!
     *
     * @param command Cmd command to be executed.
     * @return Execution std out.
     */
    default String executeCommand(String command) {
        return executeCommand(command, Collections.emptyMap());
    }

    /**
     * Executes the given command synchronously. Method <b>blocks</b> the current thread until the execution finishes!
     *
     * @param command Cmd command to be executed.
     * @param environmentVariables Environment variables key-value map.
     * @return Execution std out.
     */
    default String executeCommand(String command, Map<String, String> environmentVariables) {
        return executeCommand(command, environmentVariables, null);
    }

    /**
     * Executes the given command synchronously. Method <b>blocks</b> the current thread until the execution finishes!
     *
     * @param command Cmd command to be executed.
     * @param environmentVariables Environment variables key-value map.
     * @param workDir Directory command should be executed in.
     * @return Execution std out.
     */
    String executeCommand(String command, Map<String, String> environmentVariables, File workDir);

    /**
     * Start the given command execution. Method <b>doesn't wait</b> for the end of the command execution!
     *
     * @param command Cmd command to be executed.
     * @return Launched process.
     */
    default Process launchCommand(String command) {
        return launchCommand(command, Collections.emptyMap());
    }

    /**
     * Start the given command execution. Method <b>doesn't wait</b> for the end of the command execution!
     *
     * @param command Cmd command to be executed.
     * @param environmentVariables Environment variables key-value map.
     * @return Launched process.
     */
    default Process launchCommand(String command, Map<String, String> environmentVariables) {
        return launchCommand(command, environmentVariables, null);
    }

    /**
     * Start the given command execution. Method <b>doesn't wait</b> for the end of the command execution!
     *
     * @param command Cmd command to be executed.
     * @param environmentVariables Environment variables key-value map.
     * @param workDir Directory command should be executed in.
     * @return Launched process.
     */
    Process launchCommand(String command, Map<String, String> environmentVariables, File workDir);
}
