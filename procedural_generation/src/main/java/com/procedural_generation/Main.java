package com.procedural_generation;

import org.lwjgl.opengl.GL;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    public static void main(String[] args) {
        if (!glfwInit()) {
            System.exit(1);
        }

        long window = glfwCreateWindow(800, 600, "LWJGL 3D Example", 0, 0);
        if (window == 0) {
            throw new IllegalStateException("Failed to create window");
        }

        glfwMakeContextCurrent(window);

        // enable v-sync
        glfwSwapInterval(1);

        glfwShowWindow(window);

        GL.createCapabilities();
        glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
        glClearDepth(1.0f);

        CubeRenderer cubeRenderer = new CubeRenderer(window);
        System.out.println("Cube renderer created");

        // MAIN LOOP
        while (!glfwWindowShouldClose(window)) {
		    GL.createCapabilities();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            cubeRenderer.render();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        // clean up
        cubeRenderer.cleanup();
        glfwDestroyWindow(window);

        glfwTerminate();
    }
}