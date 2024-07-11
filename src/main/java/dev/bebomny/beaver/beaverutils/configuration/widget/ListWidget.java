//package dev.bebomny.beaver.beaverutils.configuration.widget;
//
//import com.mojang.blaze3d.platform.GlStateManager;
//import com.mojang.blaze3d.systems.RenderSystem;
//import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.gui.*;
//import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
//import net.minecraft.client.gui.screen.narration.NarrationPart;
//import net.minecraft.client.gui.tooltip.TooltipComponent;
//import net.minecraft.client.gui.widget.GridWidget;
//import net.minecraft.client.gui.widget.TextFieldWidget;
//import net.minecraft.client.render.*;
//import net.minecraft.text.Text;
//import net.minecraft.util.math.MathHelper;
//import org.jetbrains.annotations.Nullable;
//import org.lwjgl.glfw.GLFW;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Objects;
//import java.util.function.Predicate;
//
////TODO
//public class ListWidget extends AbstractParentElement implements Drawable, Selectable {
//
//    protected final MinecraftClient client;
//    private final List<Entry> children = new ArrayList<>();
//    protected int width;
//    protected int height;
//    protected int top;
//    protected int bottom;
//    protected int right;
//    protected int left;
//    private double scrollAmount;
//    private boolean renderSelection = true;
//    private boolean scrolling;
//    private Entry selected;
//    private Entry hoveredEntry;
//    public int padding = 4;
//
//    public ListWidget(int width, int height, int top, int bottom) {
//        this.client = BeaverUtilsClient.getInstance().client;
//        this.width = width;
//        this.height = height;
//        this.top = top;
//        this.bottom = bottom;
//        this.left = 0;
//        this.right = width;
//    }
//
//    public void setRenderSelection(boolean renderSelection) {
//        this.renderSelection = renderSelection;
//    }
//
//    public int getRowWidth() {
//        return Math.min(400, width - 60);
//    }
//
//    @Nullable
//    public Entry getSelectedOrNull() {
//        return this.selected;
//    }
//
//    public void setSelected(@Nullable Entry selected) {
//        this.selected = selected;
//    }
//
//    public final List<Entry> children() {
//        return this.children;
//    }
//
//    protected final void clearEntries() {
//        this.children.clear();
//    }
//
//    protected void replaceEntries(Collection<Entry> newEntries) {
//        this.children.clear();
//        this.children.addAll(newEntries);
//    }
//
//    protected Entry getEntry(int index) {
//        return this.children().get(index);
//    }
//
//    public int addEntry(Entry entry) {
//        this.children.add(entry);
//        entry.parentList = this;
//        return this.children().size() - 1;
//    }
//
//    public int getEntryCount() {
//        return this.children().size();
//    }
//
//    protected boolean isSelectedEntry(int index) {
//        return Objects.equals(this.getSelectedOrNull(), this.children().get(index));
//    }
//
//    @Nullable
//    protected final Entry getEntryAtPosition(double x, double y) {
//        int rowWidth = this.getRowWidth() / 2;
//        int midX = this.left + this.width / 2;
//        int rowLeft = midX - rowWidth;
//        int rowRight = midX + rowWidth;
//        int yRelativeToTop = MathHelper.floor(y - (double)this.top + (int)this.getScrollAmount() - 4);
//        if(x < this.getScrollbarPositionX() &&  x >= rowLeft && x <= rowRight && yRelativeToTop >= 0) {
//            int height = 0;
//            for(int i  = 0; i < this.getEntryCount(); i++) {
//                int entryHeight = getEntryHeight(i);
//                if(yRelativeToTop >= height && yRelativeToTop < height + entryHeight - padding) {
//                    return this.getEntry(i);
//                }
//                height += entryHeight;
//            }
//        }
//        return null;
//    }
//
//    public void updateSize(int width, int height, int top, int bottom) {
//        this.width = width;
//        this.height = height;
//        this.top = top;
//        this.bottom = bottom;
//        this.left = 0;
//        this.right = width;
//    }
//
//    public void setLeftPosition(int newLeft) {
//        this.left = newLeft;
//        this.right = newLeft + this.width;
//    }
//
//    protected int getMaxPosition() {
//        return this.getTotalHeight();
//    }
//
//    @Override
//    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
//        int maxScroll;//int scrollbarLeft;
//        int scrollbarThumbSize;//int scrollbarRight;
//        int scrollbarThumbPosY;
//        int scrollbarLeftPositionX = this.getScrollbarPositionX();
//        int scrollbarRightPositionX = scrollbarLeftPositionX + 6;
//        Tessellator tessellator = RenderSystem.renderThreadTesselator();
//        BufferBuilder bufferBuilder = tessellator.begin();
//
//        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
//        this.hoveredEntry = this.isMouseOver(mouseX, mouseY) ? this.getEntryAtPosition(mouseX, mouseY) : null;
//
//        //Background - currently skipping
//        /*
//        {
//            RenderSystem.setShaderTexture(0, Screen.OPTIONS_BACKGROUND_TEXTURE);
//            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
//            bufferBuilder.vertex((double)this.left, (double)this.bottom, 0.0)
//                    .texture((float)this.left / 32.0F, (float)(this.bottom + (int)this.getScrollAmount()) / 32.0F)
//                    .color(32, 32, 32, 255)
//                    .next();
//            bufferBuilder.vertex((double)this.right, (double)this.bottom, 0.0)
//                    .texture((float)this.right / 32.0F, (float)(this.bottom + (int)this.getScrollAmount()) / 32.0F)
//                    .color(32, 32, 32, 255)
//                    .next();
//            bufferBuilder.vertex((double)this.right, (double)this.top, 0.0)
//                    .texture((float)this.right / 32.0F, (float)(this.top + (int)this.getScrollAmount()) / 32.0F)
//                    .color(32, 32, 32, 255)
//                    .next();
//            bufferBuilder.vertex((double)this.left, (double)this.top, 0.0)
//                    .texture((float)this.left / 32.0F, (float)(this.top + (int)this.getScrollAmount()) / 32.0F)
//                    .color(32, 32, 32, 255)
//                    .next();
//            tessellator.draw();
//        }
//        */
//        //Background end
//
//        int rowStartX = this.getRowLeft();
//        int rowStartY = this.top + 4 - (int)this.getScrollAmount();
//        this.renderList(drawContext, rowStartX, rowStartY, mouseX, mouseY, delta);
//
//        {	// Render horizontal shadows
//            RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
//            RenderSystem.setShaderTexture(0, Screen.OPTIONS_BACKGROUND_TEXTURE);
//            RenderSystem.enableDepthTest();
//            RenderSystem.depthFunc(519);
//            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
//            bufferBuilder.vertex((double)this.left, (double)this.top, -100.0).texture(0.0F, (float)this.top / 32.0F).color(64, 64, 64, 255).next();
//            bufferBuilder.vertex((double)(this.left + this.width), (double)this.top, -100.0)
//                    .texture((float)this.width / 32.0F, (float)this.top / 32.0F)
//                    .color(64, 64, 64, 255)
//                    .next();
//            bufferBuilder.vertex((double)(this.left + this.width), 0.0, -100.0).texture((float)this.width / 32.0F, 0.0F).color(64, 64, 64, 255).next();
//            bufferBuilder.vertex((double)this.left, 0.0, -100.0).texture(0.0F, 0.0F).color(64, 64, 64, 255).next();
//            bufferBuilder.vertex((double)this.left, (double)this.height, -100.0).texture(0.0F, (float)this.height / 32.0F).color(64, 64, 64, 255).next();
//            bufferBuilder.vertex((double)(this.left + this.width), (double)this.height, -100.0)
//                    .texture((float)this.width / 32.0F, (float)this.height / 32.0F)
//                    .color(64, 64, 64, 255)
//                    .next();
//            bufferBuilder.vertex((double)(this.left + this.width), (double)this.bottom, -100.0)
//                    .texture((float)this.width / 32.0F, (float)this.bottom / 32.0F)
//                    .color(64, 64, 64, 255)
//                    .next();
//            bufferBuilder.vertex((double)this.left, (double)this.bottom, -100.0).texture(0.0F, (float)this.bottom / 32.0F).color(64, 64, 64, 255).next();
//            tessellator.draw();
//            RenderSystem.depthFunc(515);
//            RenderSystem.disableDepthTest();
//            RenderSystem.enableBlend();
//            RenderSystem.blendFuncSeparate(
//                    GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE
//            );
//            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
//            //n = 4;
//            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
//            bufferBuilder.vertex((double)this.left, (double)(this.top + 4), 0.0).color(0, 0, 0, 0).next();
//            bufferBuilder.vertex((double)this.right, (double)(this.top + 4), 0.0).color(0, 0, 0, 0).next();
//            bufferBuilder.vertex((double)this.right, (double)this.top, 0.0).color(0, 0, 0, 255).next();
//            bufferBuilder.vertex((double)this.left, (double)this.top, 0.0).color(0, 0, 0, 255).next();
//            bufferBuilder.vertex((double)this.left, (double)this.bottom, 0.0).color(0, 0, 0, 255).next();
//            bufferBuilder.vertex((double)this.right, (double)this.bottom, 0.0).color(0, 0, 0, 255).next();
//            bufferBuilder.vertex((double)this.right, (double)(this.bottom - 4), 0.0).color(0, 0, 0, 0).next();
//            bufferBuilder.vertex((double)this.left, (double)(this.bottom - 4), 0.0).color(0, 0, 0, 0).next();
//            tessellator.draw();
//        }
//
//        if ((maxScroll = this.getMaxScroll()) > 0) {
//            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
//            int scrollbarAreaHeight = this.bottom - this.top;
//            scrollbarThumbSize = (int)((float)(scrollbarAreaHeight * scrollbarAreaHeight) / (float)this.getMaxPosition());
//            scrollbarThumbSize = MathHelper.clamp(scrollbarThumbSize, 32, this.bottom - this.top - 8);
//            scrollbarThumbPosY = (int)this.getScrollAmount() * (this.bottom - this.top - scrollbarThumbSize) / maxScroll + this.top;
//            if (scrollbarThumbPosY < this.top) {
//                scrollbarThumbPosY = this.top;
//            }
//            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
//            bufferBuilder.vertex(scrollbarLeftPositionX, this.bottom, 0.0).color(0, 0, 0, 255).next();
//            bufferBuilder.vertex(scrollbarRightPositionX, this.bottom, 0.0).color(0, 0, 0, 255).next();
//            bufferBuilder.vertex(scrollbarRightPositionX, this.top, 0.0).color(0, 0, 0, 255).next();
//            bufferBuilder.vertex(scrollbarLeftPositionX, this.top, 0.0).color(0, 0, 0, 255).next();
//            bufferBuilder.vertex(scrollbarLeftPositionX, scrollbarThumbPosY + scrollbarThumbSize, 0.0).color(128, 128, 128, 255).next();
//            bufferBuilder.vertex(scrollbarRightPositionX, scrollbarThumbPosY + scrollbarThumbSize, 0.0).color(128, 128, 128, 255).next();
//            bufferBuilder.vertex(scrollbarRightPositionX, scrollbarThumbPosY, 0.0).color(128, 128, 128, 255).next();
//            bufferBuilder.vertex(scrollbarLeftPositionX, scrollbarThumbPosY, 0.0).color(128, 128, 128, 255).next();
//            bufferBuilder.vertex(scrollbarLeftPositionX, scrollbarThumbPosY + scrollbarThumbSize - 1, 0.0).color(192, 192, 192, 255).next();
//            bufferBuilder.vertex(scrollbarRightPositionX - 1, scrollbarThumbPosY + scrollbarThumbSize - 1, 0.0).color(192, 192, 192, 255).next();
//            bufferBuilder.vertex(scrollbarRightPositionX - 1, scrollbarThumbPosY, 0.0).color(192, 192, 192, 255).next();
//            bufferBuilder.vertex(scrollbarLeftPositionX, scrollbarThumbPosY, 0.0).color(192, 192, 192, 255).next();
//            tessellator.draw();
//        }
//        RenderSystem.disableBlend();
//
//        /*//////////////////////////////
//
//
//        int o;
//        int n;
//        int m;
//        int i = this.getScrollbarPositionX();
//        int j = i + 6;
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder bufferBuilder = tessellator.getBuffer();
//        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
//        this.hoveredEntry = this.isMouseOver(mouseX, mouseY) ? this.getEntryAtPosition(mouseX, mouseY) : null;
//
//        {	// Render background
//            RenderSystem.setShaderTexture(0, Screen.OPTIONS_BACKGROUND_TEXTURE);
//            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
//            bufferBuilder.vertex((double)this.left, (double)this.bottom, 0.0)
//                    .texture((float)this.left / 32.0F, (float)(this.bottom + (int)this.getScrollAmount()) / 32.0F)
//                    .color(32, 32, 32, 255)
//                    .next();
//            bufferBuilder.vertex((double)this.right, (double)this.bottom, 0.0)
//                    .texture((float)this.right / 32.0F, (float)(this.bottom + (int)this.getScrollAmount()) / 32.0F)
//                    .color(32, 32, 32, 255)
//                    .next();
//            bufferBuilder.vertex((double)this.right, (double)this.top, 0.0)
//                    .texture((float)this.right / 32.0F, (float)(this.top + (int)this.getScrollAmount()) / 32.0F)
//                    .color(32, 32, 32, 255)
//                    .next();
//            bufferBuilder.vertex((double)this.left, (double)this.top, 0.0)
//                    .texture((float)this.left / 32.0F, (float)(this.top + (int)this.getScrollAmount()) / 32.0F)
//                    .color(32, 32, 32, 255)
//                    .next();
//            tessellator.draw();
//        }
//
//        int k = this.getRowLeft();
//        int l = this.top + 4 - (int)this.getScrollAmount();
//        this.renderList(drawContext, k, l, mouseX, mouseY, delta);
//
//
//        {	// Render horizontal shadows
//            RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
//            RenderSystem.setShaderTexture(0, Screen.OPTIONS_BACKGROUND_TEXTURE);
//            RenderSystem.enableDepthTest();
//            RenderSystem.depthFunc(519);
//            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
//            bufferBuilder.vertex((double)this.left, (double)this.top, -100.0).texture(0.0F, (float)this.top / 32.0F).color(64, 64, 64, 255).next();
//            bufferBuilder.vertex((double)(this.left + this.width), (double)this.top, -100.0)
//                    .texture((float)this.width / 32.0F, (float)this.top / 32.0F)
//                    .color(64, 64, 64, 255)
//                    .next();
//            bufferBuilder.vertex((double)(this.left + this.width), 0.0, -100.0).texture((float)this.width / 32.0F, 0.0F).color(64, 64, 64, 255).next();
//            bufferBuilder.vertex((double)this.left, 0.0, -100.0).texture(0.0F, 0.0F).color(64, 64, 64, 255).next();
//            bufferBuilder.vertex((double)this.left, (double)this.height, -100.0).texture(0.0F, (float)this.height / 32.0F).color(64, 64, 64, 255).next();
//            bufferBuilder.vertex((double)(this.left + this.width), (double)this.height, -100.0)
//                    .texture((float)this.width / 32.0F, (float)this.height / 32.0F)
//                    .color(64, 64, 64, 255)
//                    .next();
//            bufferBuilder.vertex((double)(this.left + this.width), (double)this.bottom, -100.0)
//                    .texture((float)this.width / 32.0F, (float)this.bottom / 32.0F)
//                    .color(64, 64, 64, 255)
//                    .next();
//            bufferBuilder.vertex((double)this.left, (double)this.bottom, -100.0).texture(0.0F, (float)this.bottom / 32.0F).color(64, 64, 64, 255).next();
//            tessellator.draw();
//            RenderSystem.depthFunc(515);
//            RenderSystem.disableDepthTest();
//            RenderSystem.enableBlend();
//            RenderSystem.blendFuncSeparate(
//                    GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE
//            );
//            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
//            n = 4;
//            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
//            bufferBuilder.vertex((double)this.left, (double)(this.top + 4), 0.0).color(0, 0, 0, 0).next();
//            bufferBuilder.vertex((double)this.right, (double)(this.top + 4), 0.0).color(0, 0, 0, 0).next();
//            bufferBuilder.vertex((double)this.right, (double)this.top, 0.0).color(0, 0, 0, 255).next();
//            bufferBuilder.vertex((double)this.left, (double)this.top, 0.0).color(0, 0, 0, 255).next();
//            bufferBuilder.vertex((double)this.left, (double)this.bottom, 0.0).color(0, 0, 0, 255).next();
//            bufferBuilder.vertex((double)this.right, (double)this.bottom, 0.0).color(0, 0, 0, 255).next();
//            bufferBuilder.vertex((double)this.right, (double)(this.bottom - 4), 0.0).color(0, 0, 0, 0).next();
//            bufferBuilder.vertex((double)this.left, (double)(this.bottom - 4), 0.0).color(0, 0, 0, 0).next();
//            tessellator.draw();
//        }
//
//        if ((o = this.getMaxScroll()) > 0) {
//            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
//            m = (int)((float)((this.bottom - this.top) * (this.bottom - this.top)) / (float)this.getMaxPosition());
//            m = MathHelper.clamp(m, 32, this.bottom - this.top - 8);
//            n = (int)this.getScrollAmount() * (this.bottom - this.top - m) / o + this.top;
//            if (n < this.top) {
//                n = this.top;
//            }
//            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
//            bufferBuilder.vertex(i, this.bottom, 0.0).color(0, 0, 0, 255).next();
//            bufferBuilder.vertex(j, this.bottom, 0.0).color(0, 0, 0, 255).next();
//            bufferBuilder.vertex(j, this.top, 0.0).color(0, 0, 0, 255).next();
//            bufferBuilder.vertex(i, this.top, 0.0).color(0, 0, 0, 255).next();
//            bufferBuilder.vertex(i, n + m, 0.0).color(128, 128, 128, 255).next();
//            bufferBuilder.vertex(j, n + m, 0.0).color(128, 128, 128, 255).next();
//            bufferBuilder.vertex(j, n, 0.0).color(128, 128, 128, 255).next();
//            bufferBuilder.vertex(i, n, 0.0).color(128, 128, 128, 255).next();
//            bufferBuilder.vertex(i, n + m - 1, 0.0).color(192, 192, 192, 255).next();
//            bufferBuilder.vertex(j - 1, n + m - 1, 0.0).color(192, 192, 192, 255).next();
//            bufferBuilder.vertex(j - 1, n, 0.0).color(192, 192, 192, 255).next();
//            bufferBuilder.vertex(i, n, 0.0).color(192, 192, 192, 255).next();
//            tessellator.draw();
//        }
//        RenderSystem.disableBlend();
//
//        ///*/
//    }
//
//    public void centerScrollOn(Entry entry) {
//        int i = 0;
//        for(Entry e : this.children()) {
//            if(e == entry) {
//                this.setScrollAmount(i - 42);
//            }
//            i += getEntryHeight(e);
//        }
//    }
//
//    protected void ensureVisibility(Entry entry) {
//        int entryTop = this.getRowTop(this.children().indexOf(entry));
//        int spaceAbove = entryTop - this.top - 4 - entry.getHeight();
//        int spaceBelow = this.bottom - entryTop - entry.getHeight() * 2;
//
//        if (spaceAbove < 0)
//            this.scroll(spaceAbove);
//
//        if (spaceBelow < 0)
//            this.scroll(-spaceBelow);
//    }
//
//
//    private void scroll(int amount) {
//        this.setScrollAmount(this.getScrollAmount() + (double)amount);
//    }
//
//    public double getScrollAmount() {
//        return scrollAmount;
//    }
//
//    public void setScrollAmount(double newAmount) {
//        this.scrollAmount = MathHelper.clamp(newAmount, 0.0, (double)this.getMaxScroll());
//    }
//
//    public int getMaxScroll() {
//        return Math.max(0, this.getMaxPosition() - (this.bottom - this.top - 4) + 40);
//    }
//
//    protected void updateScrollingState(double mouseX, double mouseY, int button) {
//        this.scrolling = button == 0 && mouseX >= (double)this.getScrollbarPositionX() && mouseX < (double)(this.getScrollbarPositionX() - 6);
//    }
//
//    protected int getScrollbarPositionX() {
//        return this.width - 6;
//    }
//
//    public void unfocusTextField() {
//        for(Entry entry : this.children) {
//            for(Element element : entry.children()) {
//                if(element instanceof TextFieldWidget textFieldWidget) {
//                    //EmiPort.focus(textFieldWidget, false); //fix //fixed
//                    textFieldWidget.setFocused(false);
//                }
//            }
//        }
//    }
//
//    public TextFieldWidget getFocusedTextField() {
//        for(Entry entry : this.children) {
//            for(Element element : entry.children()) {
//                if(element instanceof TextFieldWidget textFieldWidget) {
//                    if(textFieldWidget.isFocused()) {
//                        return textFieldWidget;
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//        this.updateScrollingState(mouseX, mouseY, button);
//        this.unfocusTextField();
//
//        if(!isMouseOver(mouseX, mouseY)) {
//            return false;
//        }
//
//        Entry entry = getEntryAtPosition(mouseX, mouseY);
//        if(entry != null) {
//            if(entry.mouseClicked(mouseX, mouseY, button)) {
//                this.setFocused(entry);
//                this.setDragging(true);
//                return true;
//            }
//        }
//        return this.scrolling;
//    }
//
//    @Override
//    public boolean mouseReleased(double mouseX, double mouseY, int button) {
//        if(this.getFocused() != null) {
//            this.getFocused().mouseReleased(mouseX, mouseY, button);
//        }
//        return false;
//    }
//
//    @Override
//    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
//        if (super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
//            return true;
//        }
//
//        if(button != 0 || !this.scrolling) {
//            return false;
//        }
//
//        if(mouseY < (double)this.top) {
//            this.setScrollAmount(0.0f);
//        } else if(mouseY > this.bottom) {
//            this.setScrollAmount(this.getMaxScroll());
//        } else {
//            double scrollMax = Math.max(1, this.getMaxScroll());
//            int scrollAreaHeight = this.bottom - this.top;
//            int scrollThumbSize = MathHelper.clamp(
//                    (int)((float)(scrollAreaHeight * scrollAreaHeight) / (float)this.getMaxPosition()),
//                    32, scrollAreaHeight - 8);
//            double scrollSpeedFactor = Math.max(1.0, scrollMax / (double)(scrollAreaHeight - scrollThumbSize));
//            this.setScrollAmount(this.getScrollAmount() + deltaY * scrollSpeedFactor);
//        }
//
//        return true;
//    }
//
//    @Override
//    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
//        this.setScrollAmount(this.getScrollAmount() - amount * 22);
//        return true;
//    }
//
//    @Override
//    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
//        if(super.keyPressed(keyCode, scanCode, modifiers)) {
//            return true;
//        }
//
//        ///
//		if (keyCode == GLFW.GLFW_KEY_DOWN) {
//			this.moveSelection(MoveDirection.DOWN);
//			return true;
//		}
//		if (keyCode == GLFW.GLFW_KEY_UP) {
//			this.moveSelection(MoveDirection.UP);
//			return true;
//		}
//        ///
//
//        return false;
//    }
//
//    protected void moveSelection(MoveDirection direction) {
//        this.moveSelectionIf(direction, entry -> true);
//    }
//
//    protected void ensureSelectedEntryVisible() {
//        Entry entry = this.getSelectedOrNull();
//        if(entry != null) {
//            this.setSelected(entry);
//            this.ensureVisibility(entry);
//        }
//    }
//
//    protected void moveSelectionIf(MoveDirection direction, Predicate<Entry> predicate) {
//        int dirOffset = direction == MoveDirection.UP ? -1 : 1;
//
//        if(this.children().isEmpty())
//            return;
//
//        int currentIndex;
//        int selectedIndex = this.children().indexOf(this.getSelectedOrNull());
//
//        while (selectedIndex != (currentIndex  = MathHelper.clamp(selectedIndex + dirOffset, 0, this.getEntryCount() - 1))) {
//            Entry currentEntry = (Entry)this.children().get(currentIndex);
//            if(predicate.test(currentEntry)) {
//                this.setSelected(currentEntry);
//                this.ensureVisibility(currentEntry);
//                break;
//            }
//            selectedIndex = currentIndex;
//        }
//    }
//
//    @Override
//    public boolean isMouseOver(double mouseX, double mouseY) {
//        return mouseY >= (double)this.top && mouseY <= (double)this.bottom && mouseX >= (double)this.left && mouseX <= (double)this.right;
//    }
//
//    protected void renderList(DrawContext drawContext, int x, int y, int mouseX, int mouseY, float delta) {
//        int entryCount = this.getEntryCount();
//        Tessellator tessellator = RenderSystem.renderThreadTesselator();
//        BufferBuilder bufferBuilder = tessellator.getBuffer();
//
//        for(int currentEntryIndex  = 0; currentEntryIndex < entryCount; ++currentEntryIndex) {
//            int rowTop = this.getRowTop(currentEntryIndex);
//            int rowBottom = this.getRowBottom(currentEntryIndex);
//
//            if(rowBottom < this.top || rowTop > this.bottom)
//                continue;
//
//            int rowStartY = rowTop;
//            int entryHeight = getEntryHeight(currentEntryIndex);
//
//            if(entryHeight == 0)
//                continue;
//
//            entryHeight -= 4;
//            Entry entry = this.getEntry(currentEntryIndex);
//            int rowWidth = this.getRowWidth();
//
//            if(this.renderSelection && this.isSelectedEntry(currentEntryIndex)) {
//                int selectionLeft = this.left + this.width / 2 - rowWidth / 2;
//                int selectionRight = this.left + this.width / 2 + rowWidth / 2;
//
//                RenderSystem.setShader(GameRenderer::getPositionProgram);
//                float selectionOpacity = this.isFocused() ? 1.0f : 0.5f;
//
//                RenderSystem.setShaderColor(selectionOpacity, selectionOpacity, selectionOpacity, 1.0f);
//                bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
//                bufferBuilder.vertex(selectionLeft, rowStartY + entryHeight + 2, 0.0).next();
//                bufferBuilder.vertex(selectionRight, rowStartY + entryHeight + 2, 0.0).next();
//                bufferBuilder.vertex(selectionRight, rowStartY - 2, 0.0).next();
//                bufferBuilder.vertex(selectionLeft, rowStartY - 2, 0.0).next();
//                tessellator.draw();
//
//                RenderSystem.setShaderColor(0.0f, 0.0f, 0.0f, 1.0f);
//                bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
//                bufferBuilder.vertex(selectionLeft + 1, rowStartY + entryHeight + 1, 0.0).next();
//                bufferBuilder.vertex(selectionRight - 1, rowStartY + entryHeight + 1, 0.0).next();
//                bufferBuilder.vertex(selectionRight - 1, rowStartY - 1, 0.0).next();
//                bufferBuilder.vertex(selectionLeft + 1, rowStartY - 1, 0.0).next();
//                tessellator.draw();
//            }
//
//            int rowStartX = this.getRowLeft();
//            //((Entry)entry).render(draw, j, k, p, o - 3, n, mouseX, mouseY, Objects.equals(this.hoveredEntry, entry), delta); //x and y swapped
//            ((Entry) entry).render(drawContext, currentEntryIndex, rowStartX, rowTop, rowWidth - 3, entryHeight, mouseX, mouseY, Objects.equals(this.hoveredEntry, entry), delta);
//        }
//    }
//
//    public int getRowLeft() {
//        return this.left + this.width / 2 - this.getRowWidth() / 2 + 2;
//    }
//
//    public int getRowRight() {
//        return this.getRowLeft() + this.getRowWidth();
//    }
//
//    private int getEntryHeight(int i) {
//        return getEntryHeight(this.getEntry(i));
//    }
//
//    private int getEntryHeight(Entry entry) {
//        int entryHeight = entry.getHeight();
//        if (entryHeight == 0) {
//            return 0;
//        }
//        return entryHeight + padding;
//    }
//
//    protected int getRowTop(int index) {
//        int height = 0;
//        for (int i = 0; i < index; i++) {
//            height += getEntryHeight(i);
//        }
//        return this.top + 4 - (int)this.getScrollAmount() + height;
//    }
//
//    private int getRowBottom(int index) {
//        return this.getRowTop(index) + this.getEntry(index).getHeight();
//    }
//
//    public boolean isFocused() {
//        return false;
//    }
//
//    @Override
//    public SelectionType getType() {
//        if(this.isFocused()) {
//            return SelectionType.FOCUSED;
//        }
//        if(this.hoveredEntry != null) {
//            return SelectionType.HOVERED;
//        }
//        return SelectionType.NONE;
//    }
//
//    @Nullable
//    public Entry getHoveredEntry() {
//        return this.hoveredEntry;
//    }
//
//    public void setEntryParentList(Entry entry) {
//        entry.parentList = this;
//    }
//
//    protected void appendNarrations(NarrationMessageBuilder builder, Entry entry) {
//        int i;
//        List<Entry> list = this.children();
//        if (list.size() > 1 && (i = list.indexOf(entry)) != -1) {
//            builder.put(NarrationPart.POSITION, (Text)Text.translatable("narrator.position.list", i + 1, list.size()));
//        }
//    }
//
//    public int getTotalHeight() {
//        int height = 0;
//        for (int i = 0; i < this.getEntryCount(); i++) {
//            height += getEntryHeight(i);
//        }
//        if (height > 0) {
//            height -= padding;
//        }
//        return height;
//    }
//
//    @Override
//    public void appendNarrations(NarrationMessageBuilder builder) {
//
//    }
//
//
//
//    public static abstract class Entry extends AbstractParentElement {
//        public ListWidget parentList;
//
//        public abstract void render(DrawContext drawContext, int index, int x, int y, int width, int height, int mouseX, int mouseY, boolean hovered, float delta);
//
//        @Override
//        public boolean isMouseOver(double mouseX, double mouseY) {
//            return Objects.equals(this.parentList.getEntryAtPosition(mouseX, mouseY), this);
//        }
//
//        public List<TooltipComponent> getTootTip(int mouseX, int mouseY) {
//            return List.of();
//        }
//
//        public abstract int getHeight();
//    }
//
//    protected static enum MoveDirection {
//        UP,
//        DOWN;
//    }
//}
