// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package net.somyk.privatehorses.util;

import net.fabricmc.loader.api.FabricLoader;
import org.simpleyaml.configuration.comments.format.YamlCommentFormat;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;
import java.nio.file.Path;

import static net.somyk.privatehorses.PrivateHorses.*;

public class ModConfig {

    private static final String CONFIG = MOD_ID + ".yml";
    private static final Path configDir = FabricLoader.getInstance().getConfigDir();
    private static final Path configFilePath = configDir.resolve(CONFIG);

    public static void registerConfigs() {
        final YamlFile config = new YamlFile(configFilePath.toFile().getAbsolutePath());
        try {
            if (!config.exists()) {
                config.createNewFile();
                LOGGER.info("[{}]: config has been created: {}", MOD_ID, configFilePath.toFile().getPath());
            } else {
                LOGGER.info("[{}]: loading configurations..", MOD_ID);
            }
            config.loadWithComments();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        config.addDefault("message.owned_by", "%s is owned by %s");
        config.addDefault("message.new_owner", "%s is new owner of %s");
        config.addDefault("message.transfer", "%s transferred %s to you");
        config.addDefault("ignore_polymer", false);

        config.setCommentFormat(YamlCommentFormat.PRETTY);
        config.setComment("message", "Message translations");
        config.setComment("message.owned_by", "'AnimalName' is owned by 'PlayerName'");
        config.setComment("message.new_owner", "'PlayerName' is new owner of 'AnimalName'", YamlCommentFormat.BLANK_LINE);
        config.setComment("message.transfer", "'PlayerName' transferred 'AnimalName' to you", YamlCommentFormat.BLANK_LINE);
        config.setComment("ignore_polymer", "Use the above translations instead of a polymer translation", YamlCommentFormat.BLANK_LINE);

        try {
            config.save();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getStringValue(String key) {
        final YamlFile config = new YamlFile((configFilePath.toFile()).getAbsolutePath());
        try {
            config.loadWithComments();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        return config.getString(key);
    }

    public static Boolean getBooleanValue(String key) {
        final YamlFile config = new YamlFile((configFilePath.toFile()).getAbsolutePath());
        try {
            config.loadWithComments();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        return config.getBoolean(key);
    }

    public static void setValue(String key, Object newValue){
        final YamlFile config = new YamlFile((configFilePath.toFile()).getAbsolutePath());

        try {
            config.loadWithComments();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        config.set(key, newValue);

        try {
            config.save();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
