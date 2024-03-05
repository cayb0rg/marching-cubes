package com.marching_cubes;

import org.lwjgl.opengl.GL;

import static com.marching_cubes.ShaderUtils.*;
import static com.marching_cubes.VoxelGrid.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Main {
    public static int resolution = 10;

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

        // Create a camera
        Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
        Vector3f position = new Vector3f(20.0f, 20.0f, 10.0f);
        Vector3f target = new Vector3f(0.0f, 0.0f, 0.0f);
        float aspect = 800.0f / 600.0f;
        float fov = (float) Math.toRadians(45.0f);
        float near = 0.1f;
        float far = 100.0f;
        Camera camera = new Camera(position, target, up, fov, aspect, near, far, window);
        camera.yaw(-240.0f);
        camera.pitch(-40.0f);

        // Create a shader
        int shaderProgram = loadShader();

        // Initialize view and projection matrices
        int viewMatrixLocation = glGetUniformLocation(shaderProgram, "view");
        System.out.println("View matrix location: " + viewMatrixLocation);
        Matrix4f viewMatrix = camera.getViewMatrix();
        glUniformMatrix4fv(viewMatrixLocation, false, viewMatrix.get(new float[16]));

        System.out.println("Set view matrix successfully!");
        checkGLError();

        int projectionMatrixLocation = glGetUniformLocation(shaderProgram, "projection");
        System.out.println("Projection matrix location: " + projectionMatrixLocation);
        Matrix4f projectionMatrix = camera.getProjectionMatrix();
        glUniformMatrix4fv(projectionMatrixLocation, false, projectionMatrix.get(new float[16]));

        System.out.println("Set projection matrix successfully!");
        checkGLError();

        // create a voxel grid with resolution 10
        VoxelGrid voxel_grid = new VoxelGrid(10);
        Mesh mesh = new Mesh();
        ArrayList<Vector3f> positions = new ArrayList<Vector3f>();

        int x = 0;
        int y = 0;
        int z = 0;

        // MAIN LOOP
        while (!glfwWindowShouldClose(window)) {
		    GL.createCapabilities();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            if (x < resolution - 1) {
                x++;
            } else if (y < resolution - 1) {
                x = 0;
                y++;
            } else if (z < resolution - 1) {
                x = 0;
                y = 0;
                z++;
            } else {
                break;
            }

            // run marching cubes algorithm
            march_cube(x, y, z,
                voxel_grid,
                positions
            );

            // convert positions to float array
            float[] vertices = new float[positions.size() * 3];
            for (int i = 0; i < positions.size(); i++) {
                vertices[i * 3] = positions.get(i).x;
                vertices[i * 3 + 1] = positions.get(i).y;
                vertices[i * 3 + 2] = positions.get(i).z;
            }

            mesh.updateVertices(vertices);
            mesh.updateColors(vertices);

            // Set the camera position
            if (camera.mouseDragging) {
                viewMatrix = camera.getViewMatrix();
                glUniformMatrix4fv(viewMatrixLocation, false, viewMatrix.get(new float[16]));
                checkGLError();

                projectionMatrix = camera.getProjectionMatrix();
                glUniformMatrix4fv(projectionMatrixLocation, false, projectionMatrix.get(new float[16]));
                checkGLError();

                System.out.println("Set view and projection matrices successfully!");
            }
            mesh.render(shaderProgram);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        // clean up
        glfwDestroyWindow(window);
        mesh.cleanup();

        glfwTerminate();
    }
}