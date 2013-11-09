/*
 * Copyright (C) 2013, Markus Sprunck <sprunck.markus@gmail.com>
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * - The name of its contributor may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.sw_engineering_candies.big_o_test;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.common.collect.Table;

public class BigOReports {

   private static final String NL = System.getProperty("line.separator");

   public static String createDataReport(Table<Integer, String, Double> input) {
      final StringBuilder result = new StringBuilder(1000);

      // header of the table
      final Set<String> cols = input.columnKeySet();
      for (int i = 1; i < cols.size(); i++) {
         result.append("N" + i + "\t");
      }
      result.append("TIME").append(NL);

      // values of the table
      final SortedSet<Double> rows = new TreeSet<Double>();
      rows.addAll(input.column("N1").values());
      for (final Double value : rows) {
         Integer row = 0;
         for (final Integer index : input.column("N1").keySet()) {
            if (value.equals(input.get(index, "N1"))) {
               row = index;
               break;
            }
         }
         for (int col = 1; col < cols.size(); col++) {
            result.append(String.format("%.0f", input.get(row, "N" + col)) + "\t");
         }
         result.append(String.format("%.0f", input.get(row, "TIME"))).append(NL);
      }
      return result.toString();
   }

   public static String calculateBestFunction(final Table<Integer, String, Double> input) {
      // try to find all the fits
      final TreeMap<Double, String> result = BigOAnalyser.calculateBestFittingFunctions(input);
      // return best fit
      return result.get(result.descendingKeySet().first());
   }

   public static String caclulateBestFunctionsTable(final Table<Integer, String, Double> input) {
      // try to find best fits
      final TreeMap<Double, String> resultMap = BigOAnalyser.calculateBestFittingFunctions(input);
      // add the function ordered by the R^2 value of the fits
      final StringBuilder result = new StringBuilder(1000);
      result.append("TYPE      \tR^2 (adjusted)\tFUNCTION").append(NL);
      for (final Double key : resultMap.descendingKeySet()) {
         result.append(resultMap.get(key)).append(NL);
      }
      result.append(NL);
      return result.toString();
   }

}
