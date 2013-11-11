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

package com.sw_engineering_candies.big_o_test.fitter;

import java.util.Map;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealVector;

public class FitterExponential extends FitterAbstractBase {

   /**
    * Fit exponential function: Y = a0 * exp ( a1 * x )
    * 
    * see http://mathworld.wolfram.com/LeastSquaresFittingExponential.html
    */
   public void init(Map<Integer, Double> xValues_in, Map<Integer, Double> yValues_in) {

      if (xValues_in.size() <= 3) {
         return;
      }

      super.xValues = xValues_in;
      super.yValues = yValues_in;
      super.k = 2;

      calculateCoefficients();
      calculateCoefficientOfDeterminationLogarithmicData();
   }

   /**
    * Calculates the fitted polynomial for point x
    */
   @Override
   public double getY(final double x) {
      return coefficients.get(0) * Math.exp(coefficients.get(1) * x);
   }

   private void calculateCoefficients() {
      final Array2DRowRealMatrix A = new Array2DRowRealMatrix(2, 2);
      final ArrayRealVector b = new ArrayRealVector(2);
      for (int pointIndex = 1; pointIndex <= super.xValues.size(); pointIndex++) {
         final double x = super.xValues.get(pointIndex);
         final double y = super.yValues.get(pointIndex);
         A.addToEntry(0, 0, y);
         A.addToEntry(0, 1, x * y);
         A.addToEntry(1, 0, x * y);
         A.addToEntry(1, 1, x * x * y);
         b.addToEntry(0, y * Math.log(y));
         b.addToEntry(1, x * y * Math.log(y));
      }
      final DecompositionSolver solver = new LUDecomposition(A).getSolver();
      final RealVector solution = solver.solve(b);

      super.coefficients.add(0, Math.exp(solution.getEntry(0)));
      super.coefficients.add(1, solution.getEntry(1));
   }

   @Override
   public String toString() {
      final StringBuilder result = new StringBuilder(100);
      result.append(String.format("Exponential\t%.4f  \ty = ", getRSquareAdjusted()));
      result.append(String.format("%.2E", coefficients.get(0)));
      result.append(" * exp ( ");
      result.append(String.format("%.2E", coefficients.get(1)));
      result.append(" * x )");
      return result.toString();
   }

}
