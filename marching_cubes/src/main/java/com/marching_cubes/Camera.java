package com.marching_cubes;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;

import static org.lwjgl.opengl.GL11.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Vector3f position;
    private Vector3f target;
    private Vector3f up;

    private float fov; // Field of view
    private float aspect; // Aspect ratio
    private float near; // Near plane
    private float far; // Far plane

    // Mouse movement variables
    public boolean mouseDragging = false;
    private double lastX = 0.0;
    private double lastY = 0.0;
    private float yaw = 0.0f;
    private float pitch = 0.0f;

    public Camera(Vector3f position, Vector3f target, Vector3f up, float fov, float aspect, float near, float far, long window) {
        this.position = position;
        this.target = target;
        this.up = up;
        this.fov = fov;
        this.aspect = aspect;
        this.near = near;
        this.far = far;

        // Set up mouse callbacks
        glfwSetCursorPosCallback(window, this::mouseCallback);
        glfwSetMouseButtonCallback(window, this::mouseButtonCallback);
    }

    public Matrix4f getViewMatrix() {
        // Calculate camera/view matrix
        Matrix4f view = new Matrix4f()
            .rotate((float) Math.toRadians(pitch), new Vector3f(1.0f, 0.0f, 0.0f))
            .rotate((float) Math.toRadians(yaw), new Vector3f(0.0f, 1.0f, 0.0f))
            .translate(position.x, position.y, position.z);

        return view;
        // return new Matrix4f().lookAt(position, target, up);
    }

    public Matrix4f getProjectionMatrix() {
        return new Matrix4f().perspective(fov, aspect, near, far);
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

    public void setTarget(Vector3f target) {
        this.target.set(target);
    }

    public void setUp(Vector3f up) {
        this.up.set(up);
    }

    public void mouseCallback(long window, double xpos, double ypos) {
        if (mouseDragging) {
            float sensitivity = 0.1f;
            float xoffset = (float) (xpos - lastX) * sensitivity;
            float yoffset = (float) (lastY - ypos) * sensitivity;  // Reversed since y-coordinates go from bottom to top
            lastX = xpos;
            lastY = ypos;

            yaw += xoffset;
            pitch += yoffset;

            // Clamp pitch
            if (pitch > 89.0f) pitch = 89.0f;
            if (pitch < -89.0f) pitch = -89.0f;
        }
    }

    public void mouseButtonCallback(long window, int button, int action, int mods) {
        if (button == GLFW_MOUSE_BUTTON_LEFT) {
            if (action == GLFW_PRESS) {
                mouseDragging = true;
                double[] xpos = new double[1];
                double[] ypos = new double[1];
                glfwGetCursorPos(window, xpos, ypos);
                lastX = xpos[0];
                lastY = ypos[0];
            } else if (action == GLFW_RELEASE) {
                mouseDragging = false;
            }
        }
    }

    public void yaw(float amount)
	{
	    //increment the yaw by the amount param
	    yaw += amount;
	}

	//increment the camera's current yaw rotation
	public void pitch(float amount)
	{
	    //increment the pitch by the amount param
	    pitch += amount;
	}

	public void walkForward(float distance)
	{
	    position.x -= distance * (float)Math.sin(Math.toRadians(yaw));
	    position.y += distance * (float)Math.cos(Math.toRadians(yaw));
	}

	//moves the camera backward relative to its current rotation (yaw)
	public void walkBackwards(float distance)
	{
	    position.x += distance * (float)Math.sin(Math.toRadians(yaw));
	    position.y -= distance * (float)Math.cos(Math.toRadians(yaw));
	}

	//strafes the camera left relitive to its current rotation (yaw)
	public void strafeLeft(float distance)
	{
	    position.x -= distance * (float)Math.sin(Math.toRadians(yaw-90));
	    position.y += distance * (float)Math.cos(Math.toRadians(yaw-90));
	}

	//strafes the camera right relitive to its current rotation (yaw)
	public void strafeRight(float distance)
	{
	    position.x -= distance * (float)Math.sin(Math.toRadians(yaw+90));
	    position.y += distance * (float)Math.cos(Math.toRadians(yaw+90));
	}

    public void lookThrough()
    {
        //roatate the pitch around the X axis
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //roatate the yaw around the Y axis
        glRotatef(yaw, 0.0f, 0.0f, 1.0f);
        //translate to the position vector's location
        glTranslatef(position.x, position.y, position.z);
    }
}
