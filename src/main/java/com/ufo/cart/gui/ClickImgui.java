package com.ufo.cart.gui;

import com.ufo.cart.Client;
import com.ufo.cart.imgui.ImGuiImpl;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.BooleanSetting;
import com.ufo.cart.module.setting.ModeSetting;
import com.ufo.cart.module.setting.NumberSetting;
import com.ufo.cart.module.setting.Setting;
import imgui.ImGui;
import imgui.ImGuiStyle;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImFloat;
import imgui.type.ImString;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ClickImgui extends Screen {
    private final ImString searchText = new ImString(500);
    private Category category;
    private Module module;
    private boolean shouldSetKey = false;

    // Rounding and border settings matching Cyrus
    private final float[] windowRounding = {2.5f};
    private final float[] frameRounding = {3.725f};
    private final float[] windowBorderSize = {3.5f};
    private final float[] windowPadding = {10.0f, 8.0f};

    // Variables for dragging
    private boolean dragging = false;
    private float dragOffsetX = 0;
    private float dragOffsetY = 0;
    private float windowPosX = 50;
    private float windowPosY = 50;

    public ClickImgui() {
        super(Text.empty());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        setupImGuiColors();

        ImGuiImpl.render(io -> {
            ImGui.setNextWindowSize(500, 300); // Window size from Cyrus
            ImGui.setNextWindowPos(windowPosX, windowPosY); // Set position

            if (ImGui.begin("cart", ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.AlwaysAutoResize | ImGuiWindowFlags.NoResize)) {
                // Handle dragging
                if (ImGui.isMouseDragging(0)) {
                    if (!dragging) {
                        dragging = true;
                        dragOffsetX = mouseX - windowPosX;
                        dragOffsetY = mouseY - windowPosY;
                    } else {
                        windowPosX = mouseX - dragOffsetX;
                        windowPosY = mouseY - dragOffsetY;
                    }
                } else {
                    dragging = false;
                }

                // Update ImGui style
                updateStyle();

                ImGui.beginTabBar("Main");

                // Category Tabs
                for (Category category1 : Category.values()) {
                    if (ImGui.beginTabItem(category1.name + "##tab")) {
                        category = category1;
                        ImGui.endTabItem();
                    }
                }
                ImGui.endTabBar();

                if (category != null) {
                    // Module List
                    for (Module module : Client.INSTANCE.getModuleManager().getModulesInCategory(category)) {
                        ImGui.setCursorPosX(ImGui.getCursorPosX() + 20);
                        if (ImGui.collapsingHeader(module.getName())) {
                            drawToggle(module);
                            this.module = module;

                            ImGui.text(module.getDescription());
                            ImGui.sameLine();
                            if (ImGui.button(shouldSetKey ? "Listening..." : "Key " + GLFW.glfwGetKeyName(module.getKey(), 0))) {
                                shouldSetKey = true;
                            }

                            ImGui.separator();

                            // Module Settings
                            for (Setting property : module.getSettings()) {
                                if (shouldDisplayProperty(property)) {
                                    drawSetting(property);
                                }
                            }
                        } else {
                            drawToggle(module);
                        }
                    }
                }
            }
            ImGui.end();
            // Pop the style colors after rendering
            ImGui.popStyleColor(16);
        });
    }

    private void updateStyle() {
        ImGuiStyle style = ImGui.getStyle();
        style.setWindowRounding(windowRounding[0]);
        style.setFrameRounding(frameRounding[0]);
        style.setWindowBorderSize(windowBorderSize[0]);
        style.setWindowPadding(windowPadding[0], windowPadding[1]);
        style.setItemSpacing(10.0f, 4.0f); // Optional: adjust item spacing if desired
    }

    private boolean shouldDisplayProperty(Setting property) {
        return !(property.getDependencyBoolSetting() instanceof BooleanSetting depBool && !depBool.getValue()) &&
                !(property.dependencyModeSetting() instanceof ModeSetting depMode && !depMode.isMode(property.dependencyMode()));
    }

    @Override
    public boolean charTyped(char typedChar, int modifiers) {
        int keycode = charToKey(typedChar);

        if (keycode == GLFW.GLFW_KEY_ESCAPE && !shouldSetKey) {
            MinecraftClient.getInstance().setScreen(null);
            return true; // Stop further handling of this event
        }

        if (shouldSetKey) {
            module.setKey(keycode == GLFW.GLFW_KEY_ESCAPE ? 0 : keycode);
            shouldSetKey = false;
            return true; // Stop further handling of this event
        }

        return false; // Allow further handling of this event
    }

    public void drawToggle(Module module) {
        ImGui.sameLine(-16);
        ImGui.setCursorPosX(ImGui.getCursorPosX() + 20);
        if (ImGui.checkbox("##T" + module.getName(), module.isEnabled())) {
            module.toggle();
        }
    }

    private void drawSetting(Setting property) {
        if (property instanceof BooleanSetting booleanSetting) {
            if (ImGui.checkbox(property.getName(), booleanSetting.getValue())) {
                booleanSetting.setValue(!booleanSetting.getValue());
            }
        }

        if (property instanceof NumberSetting numberProperty) {
            float currentValue = (float) numberProperty.getValue();
            ImFloat imFloat = new ImFloat(currentValue);
            if (ImGui.sliderFloat("##" + numberProperty.getName(), imFloat.getData(), (float) numberProperty.getMin(), (float) numberProperty.getMax(), numberProperty.getName() + " " + imFloat.getData()[0])) {
                numberProperty.setValue(imFloat.get());
            }
        }

        if (property instanceof ModeSetting modeProperty) {
            if (ImGui.beginCombo(modeProperty.getName(), modeProperty.getMode())) {
                ImGui.inputTextWithHint("##" + modeProperty.getMode(), "Search For Modes.", searchText, ImGuiInputTextFlags.None);
                String search = searchText.get().toLowerCase();
                for (String mode : modeProperty.getModes()) {
                    if (search.isEmpty() || mode.toLowerCase().contains(search)) {
                        if (ImGui.selectable(mode)) {
                            modeProperty.setMode(mode);
                            searchText.set(new ImString(500));
                        }
                    }
                }
                ImGui.endCombo();
            }
        }
    }

    private void setupImGuiColors() {
        // Set up a sleek color scheme
        ImGui.pushStyleColor(ImGuiCol.WindowBg, 0.1f, 0.1f, 0.1f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.TitleBg, 0.15f, 0.15f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.TitleBgActive, 0.2f, 0.2f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.Tab, 0.2f, 0.2f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.TabHovered, 0.4f, 0.4f, 0.4f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.TabActive, 0.3f, 0.3f, 0.3f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.2f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.4f, 0.4f, 0.4f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.5f, 0.5f, 0.5f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.FrameBg, 0.15f, 0.15f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.FrameBgHovered, 0.25f, 0.25f, 0.25f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.FrameBgActive, 0.35f, 0.35f, 0.35f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.CheckMark, 0.7f, 0.7f, 0.7f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.SliderGrab, 0.3f, 0.3f, 0.3f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.SliderGrabActive, 0.5f, 0.5f, 0.5f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.Header, 0.2f, 0.2f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, 0.3f, 0.3f, 0.3f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.HeaderActive, 0.4f, 0.4f, 0.4f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.Separator, 0.4f, 0.4f, 0.4f, 1.0f);
    }


    private int charToKey(char character) {
        return switch (character) {
            case 'a' -> GLFW.GLFW_KEY_A;
            case 'b' -> GLFW.GLFW_KEY_B;
            case 'c' -> GLFW.GLFW_KEY_C;
            case 'd' -> GLFW.GLFW_KEY_D;
            case 'e' -> GLFW.GLFW_KEY_E;
            case 'f' -> GLFW.GLFW_KEY_F;
            case 'g' -> GLFW.GLFW_KEY_G;
            case 'h' -> GLFW.GLFW_KEY_H;
            case 'i' -> GLFW.GLFW_KEY_I;
            case 'j' -> GLFW.GLFW_KEY_J;
            case 'k' -> GLFW.GLFW_KEY_K;
            case 'l' -> GLFW.GLFW_KEY_L;
            case 'm' -> GLFW.GLFW_KEY_M;
            case 'n' -> GLFW.GLFW_KEY_N;
            case 'o' -> GLFW.GLFW_KEY_O;
            case 'p' -> GLFW.GLFW_KEY_P;
            case 'q' -> GLFW.GLFW_KEY_Q;
            case 'r' -> GLFW.GLFW_KEY_R;
            case 's' -> GLFW.GLFW_KEY_S;
            case 't' -> GLFW.GLFW_KEY_T;
            case 'u' -> GLFW.GLFW_KEY_U;
            case 'v' -> GLFW.GLFW_KEY_V;
            case 'w' -> GLFW.GLFW_KEY_W;
            case 'x' -> GLFW.GLFW_KEY_X;
            case 'y' -> GLFW.GLFW_KEY_Y;
            case 'z' -> GLFW.GLFW_KEY_Z;
            case '0' -> GLFW.GLFW_KEY_0;
            case '1' -> GLFW.GLFW_KEY_1;
            case '2' -> GLFW.GLFW_KEY_2;
            case '3' -> GLFW.GLFW_KEY_3;
            case '4' -> GLFW.GLFW_KEY_4;
            case '5' -> GLFW.GLFW_KEY_5;
            case '6' -> GLFW.GLFW_KEY_6;
            case '7' -> GLFW.GLFW_KEY_7;
            case '8' -> GLFW.GLFW_KEY_8;
            case '9' -> GLFW.GLFW_KEY_9;
            default -> GLFW.GLFW_KEY_UNKNOWN;
        };
    }
}