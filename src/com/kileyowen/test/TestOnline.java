
package com.kileyowen.test;

import java.io.IOException;

import com.kileyowen.degrees_of_separation.DijkstraControl;
import com.kileyowen.degrees_of_separation.ExceptionPageDoesNotExist;
import com.kileyowen.degrees_of_separation.Path;
import com.kileyowen.utils.ExceptionNull;

public class TestOnline {

	public static void main(final String[] args) throws ExceptionPageDoesNotExist, IOException, ExceptionNull {

		final DijkstraControl dc = new DijkstraControl(true);

		final Path path = dc.runSearch("Walton_Correctional_Institution", "Britton_Hill");

		System.out.println(path);

	}

}
