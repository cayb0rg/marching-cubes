package com.marching_cubes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.*;

public class ShaderUtils {
    public static String loadShaderSource(String filename) throws IOException {
        StringBuilder shaderSource = new StringBuilder();
        InputStream in = ClassLoader.getSystemResourceAsStream(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            shaderSource.append(line).append("\n");
        }
        reader.close();
        return shaderSource.toString();
    }
}