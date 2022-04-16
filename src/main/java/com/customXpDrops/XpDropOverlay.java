package com.customXpDrops;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.Skill;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.awt.*;

@Slf4j
public class XpDropOverlay extends Overlay {
    protected static final int RED_HIT_SPLAT_SPRITE_ID = 1359;
    protected static final float FRAMES_PER_SECOND = 50;
    protected static final String pattern = "###,###,###";
    protected static final DecimalFormat xpFormatter = new DecimalFormat(pattern);
    protected static final ArrayList<XpDropInFlight> xpDropsInFlight = new ArrayList<>();
    protected static final BufferedImage[] STAT_ICONS = new BufferedImage[Skill.values().length - 1];

    //TODO: What do the order of these indices and priority matter?
    protected static final int[] SKILL_INDICES = new int[] {10, 0, 2, 4, 6, 1, 3, 5, 16, 15, 17, 12, 20, 14, 13, 7, 11, 8, 9, 18, 19, 22, 21};
    protected static final int[] SKILL_PRIORITY = new int[] {1, 5, 2, 6, 3, 7, 4, 15, 17, 18, 0, 16, 11, 14, 13, 9, 8, 10, 19, 20, 12, 22, 21};
    protected static BufferedImage FAKE_SKILL_ICON;
    protected static BufferedImage HITSPLAT_ICON;
    protected static final float CONSTANT_FRAME_TIME = 1000.0f / FRAMES_PER_SECOND;

    protected CustomXpDropsPlugin plugin;
    protected XpDropsConfig config;

    protected String lastFont = "";
    protected int lastFontSize = 0;
    protected boolean useRunescapeFont = true;
    protected XpDropsConfig.FontStyle lastFontStyle = XpDropsConfig.FontStyle.DEFAULT;
    protected Font font = null;
    protected boolean firstRender = true;
    protected long lastFrameTime = 0;

    @Inject
    private Client client;

    //Constructor for XpDropOverlay, default to top right and to appear layer above widgets
    @Inject
    protected XpDropOverlay(CustomXpDropsPlugin plugin, XpDropsConfig config) {
        this.plugin = plugin;
        this.config = config;
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPosition(OverlayPosition.TOP_RIGHT);
    }

    //Loop through all the skills in the game to get their icons to be used
    protected void initIcons() {
        for(int i = 0; i < STAT_ICONS.length; i++) {
            STAT_ICONS[i] = plugin.getSkillIcon(Skill.values()[i]);
        }
        FAKE_SKILL_ICON = plugin.getIcon(423, 11);
        HITSPLAT_ICON = plugin.getIcon(RED_HIT_SPLAT_SPRITE_ID, 0);
    }

    //TODO: Documentation
    protected void handleFont(Graphics2D graphics) {
        if(font != null) {
            graphics.setFont(font);
            if(useRunescapeFont) {
                graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            }
        }
    }

    protected void lazyInit() {
        if(firstRender) {
            firstRender = false;
            initIcons();
        }
        if(lastFrameTime <= 0) {
            lastFrameTime = System.currentTimeMillis() - 20;
        }
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        return null;
    }
}
