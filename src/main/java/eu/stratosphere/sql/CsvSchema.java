/*
// Licensed to Julian Hyde under one or more contributor license
// agreements. See the NOTICE file distributed with this work for
// additional information regarding copyright ownership.
//
// Julian Hyde licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except in
// compliance with the License. You may obtain a copy of the License at:
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
*/
package eu.stratosphere.sql;

import net.hydromatic.optiq.*;
import net.hydromatic.optiq.impl.AbstractSchema;

import com.google.common.collect.ImmutableMap;

import java.io.*;
import java.util.*;

/**
 * Schema mapped onto a directory of CSV files. Each table in the schema
 * is a CSV file in that directory.
 */
public class CsvSchema extends AbstractSchema {
  final File directoryFile;
  private final boolean smart;

  /**
   * Creates a CSV schema.
   *
   * @param parentSchema Parent schema
   * @param name Schema name
   * @param directoryFile Directory that holds .csv files
   * @param smart      Whether to instantiate smart tables that undergo
   *                   query optimization
   */
  public CsvSchema(
      SchemaPlus parentSchema,
      String name,
      File directoryFile,
      boolean smart) {
    super(parentSchema, name);
    this.directoryFile = directoryFile;
    this.smart = smart;
  }

  @Override
  protected Map<String, Table> getTableMap() {
    final ImmutableMap.Builder<String, Table> builder = ImmutableMap.builder();
    File[] files = directoryFile.listFiles(
        new FilenameFilter() {
          public boolean accept(File dir, String name) {
            return name.endsWith(".csv");
          }
        });
    if (files == null) {
      System.out.println("directory " + directoryFile + " not found");
      files = new File[0];
    }
    for (File file : files) {
      String tableName = file.getName();
      if (tableName.endsWith(".csv")) {
        tableName = tableName.substring(
            0, tableName.length() - ".csv".length());
      }
      final CsvTable table;
      if (smart) {
        throw new RuntimeException("No smart table. Robert");
      } else {
        table = new CsvTable(file, null);
      }
      builder.put(tableName, table);
    }
    return builder.build();
  }
}

// End CsvSchema.java
