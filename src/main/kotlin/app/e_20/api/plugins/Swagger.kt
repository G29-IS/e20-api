package app.e_20.api.plugins

import app.e_20.config.ApiConfig
import app.e_20.core.logic.ObjectMapper
import app.e_20.core.logic.typedId.Id
import com.github.victools.jsonschema.generator.SchemaGenerator
import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.data.AuthType
import io.github.smiley4.ktorswaggerui.data.EncodingData
import io.github.smiley4.ktorswaggerui.data.EncodingData.Companion.schemaGeneratorConfigBuilder
import io.github.smiley4.ktorswaggerui.data.SwaggerUiSort
import io.github.smiley4.ktorswaggerui.data.SwaggerUiSyntaxHighlight
import io.ktor.server.application.*
import kotlinx.serialization.serializer


fun Application.configureSwagger() {
    install(SwaggerUI) {
        /**
         * Swagger config
         */

        swagger {
            forwardRoot = false
            swaggerUrl = "swagger"
            onlineSpecValidator()
            showTagFilterInput = true
            sort = SwaggerUiSort.HTTP_METHOD
            syntaxHighlight = SwaggerUiSyntaxHighlight.TOMORROW_NIGHT
        }

        // Needed for properly serializing Id types as normal strings
        encoding {
            val configBuilder = schemaGeneratorConfigBuilder()

            configBuilder
                .forFields()
                .withTargetTypeOverridesResolver { field ->
                    if (field.type.erasedType.interfaces.any { it == Id::class.java }) {
                        listOf(
                            field.context.resolve(String::class.java)
                        )
                    } else {
                        null
                    }
                }

            EncodingData.DEFAULT_SCHEMA_GENERATOR = SchemaGenerator(configBuilder.build())

            exampleEncoder { type, example ->
                ObjectMapper.json.encodeToString(serializer(type!!), example)
            }
        }

        /**
         * BASIC INFO
         */

        info {
            title = "E20 - OpenAPI 3.0"
            description = "This is the REST api for [E-20](https://e-20.net)"
            termsOfService = "https://e-20.net/terms"
            contact {
                email = "support@e-20.net"
            }
            version = "1.0.0"
        }

        externalDocs {
            url = "https://api-docs.e-20.net"
            description = "Official documentation"
        }


        /**
         * SERVERS
         */

        server {
            url = "https://api.e-20.net"
            description = "Stable api server"
        }

        server {
            url = "http://localhost:${ApiConfig.port}"
            description = "Local development server"
        }

        /**
         * SECURITY
         */

        securityScheme(AuthenticationMethods.USER_SESSION_AUTH) {
            type = AuthType.HTTP
        }

        defaultSecuritySchemeName = AuthenticationMethods.USER_SESSION_AUTH

        defaultUnauthorizedResponse {
            description = "Invalid session"
        }

        /**
         * SCHEMAS
         */
        customSchemas {
            json("jwt-token-response-schema") {
                """
                    {
                        "type": "object",
                        "properties": {
                            "token": {
                                "type": "string"
                            }
                        },
                        "required": [
                            "token"
                        ]
                    }
                """.trimIndent()
            }
        }
    }
}