/*
 * Copyright 2013 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.rendering.assets.texture;

import org.terasology.asset.AssetData;
import org.terasology.math.TeraMath;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @author Immortius
 */
public class TextureData implements AssetData {
    private static final int BYTES_PER_PIXEL = 4;

    private int width;
    private int height;
    private int depth = 1;
    private Texture.WrapMode wrapMode = Texture.WrapMode.CLAMP;
    private Texture.FilterMode filterMode = Texture.FilterMode.NEAREST;
    private Texture.Type type = Texture.Type.TEXTURE2D;
    private ByteBuffer[] data;

    public TextureData(int width, int height, int depth, ByteBuffer[] mipmaps, Texture.WrapMode wrapMode, Texture.FilterMode filterMode) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.wrapMode = wrapMode;
        this.filterMode = filterMode;
        this.type = Texture.Type.TEXTURE3D;
        this.data = Arrays.copyOf(mipmaps, mipmaps.length);

        if (width <= 0 || height <= 0 || depth <= 0) {
            throw new IllegalArgumentException("Width, height and depth must be positive");
        }
        if (mipmaps.length == 0) {
            throw new IllegalArgumentException("Must supply at least one mipmap");
        }
        if (mipmaps[0].limit() != width * height * depth * BYTES_PER_PIXEL) {
            throw new IllegalArgumentException("Texture data size incorrect, must be a set of RGBA values for each pixel (width * height * depth)");
        }
        if (mipmaps.length > 1 && !(TeraMath.isPowerOfTwo(width) && TeraMath.isPowerOfTwo(height) && TeraMath.isPowerOfTwo(depth))) {
            throw new IllegalArgumentException("Texture width, height and depth must be powers of 2 for mipmapping");
        }
        for (int i = 1; i < mipmaps.length; ++i) {
            int mipWidth = width >> i;
            int mipHeight = height >> i;
            int mipDepth = depth >> i;
            if (mipWidth * mipHeight * mipDepth * BYTES_PER_PIXEL != mipmaps[i].limit()) {
                throw new IllegalArgumentException("Mipmap has wrong dimensions");
            }
        }
    }

    public TextureData(int width, int height, ByteBuffer[] mipmaps, Texture.WrapMode wrapMode, Texture.FilterMode filterMode) {
        this.width = width;
        this.height = height;
        this.wrapMode = wrapMode;
        this.filterMode = filterMode;
        this.data = Arrays.copyOf(mipmaps, mipmaps.length);
        this.type = Texture.Type.TEXTURE2D;

        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
        if (mipmaps.length == 0) {
            throw new IllegalArgumentException("Must supply at least one mipmap");
        }
        if (mipmaps[0].limit() != width * height * BYTES_PER_PIXEL) {
            throw new IllegalArgumentException("Texture data size incorrect, must be a set of RGBA values for each pixel (width * height)");
        }
        if (mipmaps.length > 1 && !(TeraMath.isPowerOfTwo(width) && TeraMath.isPowerOfTwo(height))) {
            throw new IllegalArgumentException("Texture width and height must be powers of 2 for mipmapping");
        }
        for (int i = 1; i < mipmaps.length; ++i) {
            int mipWidth = width >> i;
            int mipHeight = height >> i;
            if (mipWidth * mipHeight * BYTES_PER_PIXEL != mipmaps[i].limit()) {
                throw new IllegalArgumentException("Mipmap has wrong dimensions");
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }

    public Texture.Type getType() {
        return type;
    }

    public Texture.WrapMode getWrapMode() {
        return wrapMode;
    }

    public Texture.FilterMode getFilterMode() {
        return filterMode;
    }

    public ByteBuffer[] getBuffers() {
        return Arrays.copyOf(data, data.length);
    }
}
