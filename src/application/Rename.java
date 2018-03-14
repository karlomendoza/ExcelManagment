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
		File filesToRename = new File("C:\\Users\\Karlo Mendoza\\Excel Work\\ICU MEDICAL\\Enovia\\T1_\\UPLOAD");

		// Rename the files to have it's changeOrder prepended in the name
		List<String> ecos = new ArrayList<>();
		BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\Karlo Mendoza\\Excel Work\\ICU MEDICAL\\Enovia\\T1_\\ecos.txt"));

		String prepededText = "T1_";

		String line;
		while ((line = in.readLine()) != null) {
			ecos.add(line);
		}
		in.close();

		int i = 0;
		for (File file : filesToRename.listFiles()) {
			if (file.isDirectory() || file.getName().equals("indexFile.txt"))
				continue;

			String name = file.getName();
			String shortName = name.replaceAll(prepededText, "");

			Files.move(Paths.get(file.getAbsolutePath()), Paths.get(file.getParentFile() + "\\" + prepededText + ecos.get(i) + "_" + shortName));
			i++;
		}
	}

}
