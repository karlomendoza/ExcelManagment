package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Rename {

	public static void main(String... strings) throws IOException {
		// Rename the files to have it's changeOrder prepended in the name
		File filesToRename = new File(
				"C:\\Users\\Karlo Mendoza\\Excel Work\\ICU MEDICAL\\SAP DMS\\T1\\T1_upload_files\\UPLOAD");

		// Rename the files to have it's changeOrder prepended in the name
		List<String> ecos = new ArrayList<>();
		BufferedReader in = new BufferedReader(
				new FileReader("C:\\Users\\Karlo Mendoza\\Excel Work\\ICU MEDICAL\\SAP DMS\\ecos.txt"));
		String line;
		while ((line = in.readLine()) != null) {
			ecos.add(line);
		}
		in.close();

		int i = 0;
		for (File file : filesToRename.listFiles()) {

			String name = file.getName();
			String shortName = name.replaceAll("T1_", "");

			Files.move(Paths.get(file.getAbsolutePath()),
					Paths.get(file.getParentFile() + "\\T1_" + ecos.get(i) + "_" + shortName));
			i++;
		}
	}

}
