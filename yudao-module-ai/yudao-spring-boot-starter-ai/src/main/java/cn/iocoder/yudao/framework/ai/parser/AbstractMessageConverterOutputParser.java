/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.iocoder.yudao.framework.ai.parser;

import org.springframework.messaging.converter.MessageConverter;

/**
 * Abstract {@link OutputParser} implementation that uses a pre-configured
 * {@link MessageConverter} to convert the LLM output into the desired type format.
 *
 * @param <T> Specifies the desired response type.
 * @author Mark Pollack
 * @author Christian Tzolov
 */
public abstract class AbstractMessageConverterOutputParser<T> implements OutputParser<T> {

	private MessageConverter messageConverter;

	public AbstractMessageConverterOutputParser(MessageConverter messageConverter) {
		this.messageConverter = messageConverter;
	}

	public MessageConverter getMessageConverter() {
		return this.messageConverter;
	}

}