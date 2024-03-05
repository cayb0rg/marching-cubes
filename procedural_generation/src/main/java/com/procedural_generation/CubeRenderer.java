package com.procedural_generation;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.BufferUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31C.glGetActiveUniformName;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryStack.*;

public class CubeRenderer {
    // Vertices for a unit cube centered at the origin
    private static final float[] positions = new float[] {
        // VO
        -0.5f,  0.5f,  0.5f,
        // V1
        -0.5f, -0.5f,  0.5f,
        // V2
        0.5f, -0.5f,  0.5f,
        // V3
         0.5f,  0.5f,  0.5f,
        // V4
        -0.5f,  0.5f, -0.5f,
        // V5
         0.5f,  0.5f, -0.5f,
        // V6
        -0.5f, -0.5f, -0.5f,
        // V7
         0.5f, -0.5f, -0.5f,
    };

    private static final float[] colors = new float[]{
        0.5f, 0.0f, 0.0f,
        0.0f, 0.5f, 0.0f,
        0.0f, 0.0f, 0.5f,
        0.0f, 0.5f, 0.5f,
        0.5f, 0.0f, 0.0f,
        0.0f, 0.5f, 0.0f,
        0.0f, 0.0f, 0.5f,
        0.0f, 0.5f, 0.5f,
    };


    // Indices to define triangles for rendering a cube
    private static final int[] indices = new int[] {
        // Front face
        0, 1, 3, 3, 1, 2,
        // Top Face
        4, 0, 3, 5, 4, 3,
        // Right face
        3, 2, 7, 5, 3, 7,
        // Left face
        6, 1, 0, 6, 0, 4,
        // Bottom face
        2, 1, 6, 2, 6, 7,
        // Back face
        7, 6, 4, 7, 4, 5,
    };

    // Vertex buffer object (VBO) ID
    private int posVboId;

    private int colorVboId;

    // Vertex array object (VAO) ID
    private int vao;

    // Index VBO
    private int idxVboId;

    // Shader program ID
    private int shaderProgram;

    private Camera camera;

    private int element_count;

    // Constructor
    public CubeRenderer(long window) {
        loadShader();

        stackPush();

        element_count = 3;

        // Generate VAO
        vao = glGenVertexArrays(); // generate a VAO ID
        glBindVertexArray(vao); // bind the VAO

        try (MemoryStack stack = stackPush()) {
            FloatBuffer buffer = stackMallocFloat(positions.length);
            buffer.put(positions);
            buffer.flip();
            posVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, posVboId);
            glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        }
        // Enable the position attribute
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        try (MemoryStack stack = stackPush()) {
            FloatBuffer buffer = stackMallocFloat(colors.length);
            buffer.put(colors);
            buffer.flip();
            colorVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, colorVboId);
            glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        }
        // Enable the color attribute
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

        // Create indices buffer
        try (MemoryStack stack = stackPush()) {
            idxVboId = glGenBuffers();
            IntBuffer buffer = stackMallocInt(indices.length);
            buffer.put(indices);
            buffer.flip();
            glBindBuffer(GL_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        }

        glBindVertexArray(0);

        // Set view and projection matrices
        Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
        Vector3f position = new Vector3f(3.0f, 3.0f, 3.0f);
        Vector3f target = new Vector3f(0.0f, 0.0f, 0.0f);
        float aspect = 800.0f / 600.0f;
        float fov = (float) Math.toRadians(45.0f);
        float near = 0.1f;
        float far = 100.0f;
        camera = new Camera(position, target, up, fov, aspect, near, far, window);

        /* DEBUGGING START */
        // Get the total number of active uniforms in the shader program
        int numActiveUniforms = glGetProgrami(shaderProgram, GL_ACTIVE_UNIFORMS);
        System.out.println("  numActiveUniforms: " + numActiveUniforms);

        // Buffer to hold uniform information
        IntBuffer sizeBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer typeBuffer = BufferUtils.createIntBuffer(1);
        for (int i = 0; i < numActiveUniforms; i++) {
            // Get information about the i-th uniform
            glGetActiveUniform(shaderProgram, i, sizeBuffer, typeBuffer);

            // Get the size and type of the uniform
            int size = sizeBuffer.get(0);
            int type = typeBuffer.get(0);

            // Get the name of the uniform
            String name = glGetActiveUniformName(shaderProgram, i);

            // Print information about the uniform
            System.out.println("Uniform " + i + ":");
            System.out.println("  Name: " + name);
            System.out.println("  Size: " + size);
            System.out.println("  Type: " + type);
        }
        /* DEBUGGING END */

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

    public void loadShader() {
        // shader program
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

        try {
            String vertexShaderSource = ShaderUtils.loadShaderSource("com/procedural_generation/shaders/vertShader.glsl");
            String fragmentShaderSource = ShaderUtils.loadShaderSource("com/procedural_generation/shaders/fragShader.glsl");

            // Load and compile vertex shader
            glShaderSource(vertexShader, vertexShaderSource);
            glCompileShader(vertexShader);
            checkGLError();
            String vertexCompileLog = glGetShaderInfoLog(vertexShader);
            System.out.println("Vertex shader compilation log:\n" + vertexCompileLog);

            // Load and compile fragment shader
            glShaderSource(fragmentShader, fragmentShaderSource);
            glCompileShader(fragmentShader);
            checkGLError();
            String fragmentCompileLog = glGetShaderInfoLog(fragmentShader);
            System.out.println("Fragment shader compilation log:\n" + fragmentCompileLog);

            System.out.println("Compiled shaders successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);
        checkGLError();
        String programLinkLog = glGetProgramInfoLog(shaderProgram);
        System.out.println("Shader program linking log:\n" + programLinkLog);
    }

    // Render method
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
        // glDrawArrays(GL_TRIANGLES, 0, element_count);
        checkGLError();
        glDrawElements(GL_TRIANGLES, positions.length / 3, GL_UNSIGNED_INT, 0);
        checkGLError();
        System.out.println("Rendered cube successfully!");


        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        // Unbind VAO
        glBindVertexArray(0);
    }

    public static void checkGLError() {
        int err = glGetError();

        while (err != GL_NO_ERROR) {
            String error;
            switch (err) {
                case GL_INVALID_OPERATION:
                    error = "INVALID_OPERATION";
                    break;
                case GL_INVALID_ENUM:
                    error = "INVALID_ENUM";
                    break;
                case GL_INVALID_VALUE:
                    error = "INVALID_VALUE";
                    break;
                case GL_OUT_OF_MEMORY:
                    error = "OUT_OF_MEMORY";
                    break;
                case GL_INVALID_FRAMEBUFFER_OPERATION:
                    error = "INVALID_FRAMEBUFFER_OPERATION";
                    break;
                case GL_STACK_UNDERFLOW:
                    error = "STACK_UNDERFLOW";
                    break;
                case GL_STACK_OVERFLOW:
                    error = "STACK_OVERFLOW";
                    break;
                case GL_NO_ERROR:
                    error = "NO_ERROR";
                    break;
                default:
                    error = "UNKNOWN_ERROR";
                    break;
            }
            System.out.println("GL_" + error);
            err = glGetError();
        }
    }

    // Cleanup method
    public void cleanup() {
        glDisableVertexAttribArray(0);
        // Delete VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(posVboId);
        glDeleteBuffers(colorVboId);
        glDeleteBuffers(idxVboId);
        // Delete VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vao);
    }
}

