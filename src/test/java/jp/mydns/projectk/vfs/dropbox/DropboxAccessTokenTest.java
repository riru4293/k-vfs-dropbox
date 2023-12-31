/*
 * Copyright (c) 2024, Project-K
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package jp.mydns.projectk.vfs.dropbox;

import jakarta.json.Json;
import jakarta.json.JsonValue;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.UserAuthenticationData;
import static org.apache.commons.vfs2.UserAuthenticationData.PASSWORD;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import org.junit.jupiter.api.Test;

/**
 * Test of class DropboxAccessToken.
 *
 * @author riru
 * @version 1.0.0
 * @since 1.0.0
 */
class DropboxAccessTokenTest {

    /**
     * Test constructor. If argument is valid {@code JsonValue}.
     *
     * @since 1.0.0
     */
    @Test
    void testConstructor_JsonValue() {

        assertThat(new DropboxAccessToken(Json.createValue("UFO")).getValue()).isEqualTo(Json.createValue("UFO"));

    }

    /**
     * Test constructor. If argument is illegal {@code JsonValue}.
     *
     * @since 1.0.0
     */
    @Test
    void testConstructor_IllegalJsonValue() {

        assertThatIllegalArgumentException().isThrownBy(() -> new DropboxAccessToken(JsonValue.NULL))
                .withMessage("FileOption value of [%s] must be string.", "dropbox:accessToken");

    }

    /**
     * Test constructor. If argument is valid {@code String}.
     *
     * @since 1.0.0
     */
    @Test
    void testConstructor_String() {

        assertThat(new DropboxAccessToken("K").getValue()).isEqualTo(Json.createValue("K"));

    }

    /**
     * Test apply method.
     *
     * @since 1.0.0
     */
    @Test
    void testApply() throws FileSystemException {

        FileSystemOptions opts = new FileSystemOptions();

        new DropboxAccessToken.Resolver().newInstance(Json.createValue("token")).apply(opts);

        var b = DefaultFileSystemConfigBuilder.getInstance();
        var ua = b.getUserAuthenticator(opts);
        var uad = ua.requestAuthentication(new UserAuthenticationData.Type[]{PASSWORD});

        assertThat(uad.getData(PASSWORD)).containsExactly('t', 'o', 'k', 'e', 'n');

        new DropboxAccessToken.Resolver().newInstance(Json.createValue("p")).apply(opts);

        var ua2 = b.getUserAuthenticator(opts);
        var uad2 = ua2.requestAuthentication(new UserAuthenticationData.Type[]{PASSWORD});

        assertThat(uad2.getData(PASSWORD)).containsExactly('p');

    }

    /**
     * Test {@code equals} method and {@code hashCode} method.
     *
     * @since 1.0.0
     */
    @Test
    void testEqualsHashCode() {

        DropboxAccessToken base = new DropboxAccessToken(" ");
        DropboxAccessToken same = new DropboxAccessToken(" ");
        DropboxAccessToken another = new DropboxAccessToken("");

        assertThat(base).hasSameHashCodeAs(same).isEqualTo(same)
                .doesNotHaveSameHashCodeAs(another).isNotEqualTo(another);

    }

    /**
     * Test of toString method.
     *
     * @since 1.0.0
     */
    @Test
    void testToString() {

        var result = new DropboxAccessToken("Hell").toString();

        assertThat(result).isEqualTo("{\"dropbox:accessToken\":\"Hell\"}");

    }
}
