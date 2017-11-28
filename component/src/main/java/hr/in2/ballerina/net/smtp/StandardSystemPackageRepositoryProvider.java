/*
 * Copyright (c) 2017, IN2 Ltd. (http://www.in2.hr) All Rights Reserved.
 *
 * IN2 Ltd. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package hr.in2.ballerina.azure.iotdevice;

import org.ballerinalang.annotation.JavaSPIService;
import org.ballerinalang.repository.PackageRepository;
import org.ballerinalang.repository.fs.ClasspathPackageRepository;
import org.ballerinalang.spi.SystemPackageRepositoryProvider;

/**
 * This represents the standard Ballerina built-in system package repository provider.
 */
@JavaSPIService("org.ballerinalang.spi.SystemPackageRepositoryProvider")
public class StandardSystemPackageRepositoryProvider implements SystemPackageRepositoryProvider {

    private static final String JAR_SYSTEM_LIB_LOCATION = "/META-INF/natives/";

    @Override
    public PackageRepository loadRepository() {
        return new ClasspathPackageRepository(this.getClass(), JAR_SYSTEM_LIB_LOCATION);
    }

}
