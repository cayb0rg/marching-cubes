package com.marching_cubes;

import org.lwjgl.opengl.GL;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.joml.Vector3f;

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

        glEnable(GL_DEPTH_TEST);

        // CubeRenderer cubeRenderer = new CubeRenderer(window);
        // System.out.println("Cube renderer created");

        VoxelGrid voxel_grid = new VoxelGrid(10);
        ArrayList<Vector3f> positions = voxel_grid.create_grid();
        float[] vertices = new float[positions.size() * 3];
        for (int i = 0; i < positions.size(); i++) {
            vertices[i * 3] = positions.get(i).x;
            vertices[i * 3 + 1] = positions.get(i).y;
            vertices[i * 3 + 2] = positions.get(i).z;
        }
        Mesh mesh = new Mesh(vertices, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, window);

        // MAIN LOOP
        while (!glfwWindowShouldClose(window)) {
		    GL.createCapabilities();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // cubeRenderer.render();
            // glBegin(GL_TRIANGLES);
            // for (int i = 0; i < positions.size(); i += 3) {
            //     Vector3f v1 = positions.get(i);
            //     Vector3f v2 = positions.get(i + 1);
            //     Vector3f v3 = positions.get(i + 2);

            //     glVertex3f(v1.x, v1.y, v1.z);
            //     glVertex3f(v2.x, v2.y, v2.z);
            //     glVertex3f(v3.x, v3.y, v3.z);
            // }
            // glEnd();
            mesh.render();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        // clean up
        // cubeRenderer.cleanup();
        glfwDestroyWindow(window);

        glfwTerminate();
    }
}