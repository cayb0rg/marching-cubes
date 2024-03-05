package com.marching_cubes;

import static com.marching_cubes.ShaderUtils.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

public class Mesh {
    public float[] vertices;
    public float[] colors;
    public int[] indices;

    public int shaderProgram;
    public long window;
    public Camera camera;
    public int vao;
    public int posVboId = -1;
    public int colorVboId = -1;
    public int idxVboId = -1;

    public Mesh(float[] vertices, int[] indices, long window) {
        this.vertices = vertices;
        this.indices = indices;

        shaderProgram = loadShader();

        // Generate VAO
        vao = generateVAO();

        if (vertices.length > 0) {
            posVboId = generateVBOFloat(vertices);
            // Enable the position attribute
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        }
        if (colors != null && colors.length > 0) {
            colorVboId = generateVBOFloat(colors);
            // Enable the color attribute
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
        }
        if (indices.length > 0) {
            idxVboId = generateVBOInt(indices);
        }

        glBindVertexArray(0);

        Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
        Vector3f position = new Vector3f(3.0f, 3.0f, 3.0f);
        Vector3f target = new Vector3f(0.0f, 0.0f, 0.0f);
        float aspect = 800.0f / 600.0f;
        float fov = (float) Math.toRadians(45.0f);
        float near = 0.1f;
        float far = 100.0f;
        camera = new Camera(position, target, up, fov, aspect, near, far, window);

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
    }

    public void render() {
        glUseProgram(shaderProgram);
        // Bind VAO
        glBindVertexArray(vao);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Set the camera position
        if (camera.mouseDragging) {
            int viewMatrixLocation = glGetUniformLocation(shaderProgram, "view");
            Matrix4f viewMatrix = camera.getViewMatrix();
            glUniformMatrix4fv(viewMatrixLocation, false, viewMatrix.get(new float[16]));
            checkGLError();

            int projectionMatrixLocation = glGetUniformLocation(shaderProgram, "projection");
            Matrix4f projectionMatrix = camera.getProjectionMatrix();
            glUniformMatrix4fv(projectionMatrixLocation, false, projectionMatrix.get(new float[16]));
            checkGLError();

            System.out.println("Set view and projection matrices successfully!");
        }
        // Calculate translation for each cube
        float x = 0 * 2.0f; // Adjust as needed
        float y = 0.0f;     // Adjust as needed
        float z = 0.0f;     // Adjust as needed

        // Set the translation matrix
        Matrix4f translationMatrix = new Matrix4f().translate(x, y, z);

        // Set scale matrix
        Matrix4f scaleMatrix = new Matrix4f().scale(1.0f);

        // Set rotation matrix
        Matrix4f rotationMatrix = new Matrix4f().identity();
        // Rotate the cube around the y-axis
        rotationMatrix.rotate((float) Math.toRadians(90.0f), new Vector3f(0.0f, 1.0f, 0.0f));

        // Create model matrix
        Matrix4f modelMatrix = new Matrix4f().identity().mul(translationMatrix).mul(scaleMatrix);
        System.out.println("Model matrix: " + modelMatrix.toString());


        System.out.println("Set translation matrix successfully!");
        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
        System.out.println("Created matrix buffer successfully!");
        modelMatrix.get(matrixBuffer);
        System.out.println("Got matrix buffer successfully!");

        // Upload the translation matrix to the shader
        int matrixLocation = glGetUniformLocation(shaderProgram, "model");
        System.out.println("Got matrix location successfully! " + matrixLocation);
        glUniformMatrix4fv(matrixLocation, false, matrixBuffer);
        checkGLError();
        System.out.println("Uploaded matrix to shader successfully!");

        // Render cube
        glDrawArrays(GL_TRIANGLES, 0, vertices.length);
        System.out.println("Rendered cube successfully!");


        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        // Unbind VAO
        glBindVertexArray(0);
    }


    // Cleanup method
    public void cleanup() {
        glDisableVertexAttribArray(0);
        // Delete VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        if (posVboId != -1)
            glDeleteBuffers(posVboId);
        if (colorVboId != -1)
            glDeleteBuffers(colorVboId);
        if (idxVboId != -1)
            glDeleteBuffers(idxVboId);
        // Delete VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vao);
    }
}
