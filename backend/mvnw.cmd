@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script, version 3.2.0
@REM ----------------------------------------------------------------------------

@IF "%__MVNW_ARG0__%"=="" (SET __MVNW_ARG0__=%~f0)
@SET __MVNW_CMD__=
@SET __MVNW_ERROR__=
@SET __MVNW_PSMODULEP_SAVE__=%PSModulePath%
@SET PSModulePath=
@FOR /F "usebackq tokens=1* delims==" %%A IN (`powershell -noprofile "& {(Get-Command -CommandType Application powershell | Select-Object -First 1).Path}"`) DO @SET __MVNW_PSMODULEP__=%%~dpA
@IF "%__MVNW_PSMODULEP__%"=="" (GOTO fallback)
@SET "PSModulePath=%__MVNW_PSMODULEP__%;%__MVNW_PSMODULEP_SAVE__%"
@SET __MVNW_PSMODULEP_SAVE__=

@SET __MVNW_CMD__=powershell -noprofile -executionpolicy bypass -file "%~dp0mvnw.ps1" %*
@GOTO :main

:fallback
@SET PSModulePath=%__MVNW_PSMODULEP_SAVE__%
@SET __MVNW_PSMODULEP_SAVE__=
:bootstrap
@REM Bootstrap Maven Wrapper
@SET __MVNW_CMD__=powershell -noprofile -command "&{Import-Module BitsTransfer; Start-BitsTransfer -Source 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar' -Destination '%~dp0.mvn\wrapper\maven-wrapper.jar'}"
@CALL %__MVNW_CMD__%
@SET __MVNW_CMD__=

:main
@IF NOT EXIST "%~dp0.mvn\wrapper\maven-wrapper.jar" (
    @ECHO Downloading Maven Wrapper...
    @powershell -noprofile -command "&{Import-Module BitsTransfer; Start-BitsTransfer -Source 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar' -Destination '%~dp0.mvn\wrapper\maven-wrapper.jar'}"
)

@SET WRAPPER_JAR="%~dp0.mvn\wrapper\maven-wrapper.jar"
@SET WRAPPER_PROPERTIES="%~dp0.mvn\wrapper\maven-wrapper.properties"

@IF NOT EXIST %WRAPPER_JAR% (
    @ECHO ERROR: Could not find maven-wrapper.jar
    @ECHO Please run this script from the project root directory
    @EXIT /B 1
)

@REM Find Java
@SET JAVA_EXE=java
@IF NOT "%JAVA_HOME%"=="" SET JAVA_EXE="%JAVA_HOME%\bin\java"

@REM Run Maven
%JAVA_EXE% -jar %WRAPPER_JAR% %*
