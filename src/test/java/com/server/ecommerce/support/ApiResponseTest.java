package com.server.ecommerce.support;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class ApiResponseTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@DisplayName("OK 메서드는 기본 메시지와 상태코드 200 응답을 생성한다.")
	void ok_with_default_message() {
		// arrange
		String testData = "test data";

		// act
		ResponseEntity<ApiResponse<String>> response = ApiResponse.OK(testData);

		// assert
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody().getMessage()).isEqualTo("요청이 성공적으로 처리되었습니다.");
		assertThat(response.getBody().getData()).isEqualTo(testData);
	}

	@Test
	@DisplayName("OK 메서드는 커스텀 메시지와 함께 200 응답을 생성한다")
	void ok_with_custom_message() {
		// arrange
		String testData = "test data";
		String customMessage = "조회가 완료되었습니다.";

		// act
		ResponseEntity<ApiResponse<String>> response = ApiResponse.OK(customMessage, testData);

		// assert
		assertThat(response.getStatusCode()).isEqualTo(OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getHttpStatus()).isEqualTo(OK);
		assertThat(response.getBody().getHttpStatusCode()).isEqualTo(200);
		assertThat(response.getBody().getMessage()).isEqualTo(customMessage);
		assertThat(response.getBody().getData()).isEqualTo(testData);
	}

	@Test
	@DisplayName("CREATE 메서드는 기본 메시지와 함께 201 응답을 생성한다")
	void create_with_default_message() {
		// arrange
		String testData = "created data";

		// act
		ResponseEntity<ApiResponse<String>> response = ApiResponse.CREATE(testData);

		// assert
		assertThat(response.getStatusCode()).isEqualTo(CREATED);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getHttpStatus()).isEqualTo(CREATED);
		assertThat(response.getBody().getHttpStatusCode()).isEqualTo(201);
		assertThat(response.getBody().getMessage()).isEqualTo(ApiResponse.DEFAULT_MESSAGE);
		assertThat(response.getBody().getData()).isEqualTo(testData);
	}

	@Test
	@DisplayName("CREATE 메서드는 커스텀 메시지와 함께 201 응답을 생성한다")
	void create_with_custom_message() {
		// arrange
		String testData = "created data";
		String customMessage = "상품이 성공적으로 생성되었습니다.";

		// act
		ResponseEntity<ApiResponse<String>> response = ApiResponse.CREATE(customMessage, testData);

		// assert
		assertThat(response.getStatusCode()).isEqualTo(CREATED);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getHttpStatus()).isEqualTo(CREATED);
		assertThat(response.getBody().getHttpStatusCode()).isEqualTo(201);
		assertThat(response.getBody().getMessage()).isEqualTo(customMessage);
		assertThat(response.getBody().getData()).isEqualTo(testData);
	}

	@Test
	@DisplayName("제네릭 타입으로 다양한 데이터 타입을 지원한다")
	void generic_support_various_data_types() {
		// arrange
		List<String> listData = List.of("item1", "item2", "item3");
		Integer integerData = 123;

		// act
		ResponseEntity<ApiResponse<List<String>>> listResponse = ApiResponse.OK(listData);
		ResponseEntity<ApiResponse<Integer>> integerResponse = ApiResponse.OK(integerData);

		// assert
		assertThat(listResponse.getBody().getData()).isEqualTo(listData);
		assertThat(integerResponse.getBody().getData()).isEqualTo(integerData);
	}

	@Test
	@DisplayName("null 데이터도 정상적으로 처리한다")
	void handle_null_data() {
		// act
		ResponseEntity<ApiResponse<String>> response = ApiResponse.OK((String) null);

		// assert
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getData()).isNull();
		assertThat(response.getBody().getHttpStatusCode()).isEqualTo(200);
	}

	@Test
	@DisplayName("ApiResponse는 JSON으로 올바르게 직렬화된다")
	void json_serialization() throws JsonProcessingException {
		// arrange
		TestDto testDto = new TestDto("test", 123);
		ApiResponse<TestDto> apiResponse = new ApiResponse<>(
			OK,
			200,
			"테스트 메시지",
			testDto
		);

		// act
		String json = objectMapper.writeValueAsString(apiResponse);

		// assert
		assertThat(json).contains("\"httpStatus\":\"OK\"");
		assertThat(json).contains("\"httpStatusCode\":200");
		assertThat(json).contains("\"message\":\"테스트 메시지\"");
		assertThat(json).contains("\"name\":\"test\"");
		assertThat(json).contains("\"value\":123");
	}

	@Test
	@DisplayName("불변 객체로서 생성 후 상태 변경이 불가능하다")
	void immutable_object() {
		// arrange
		String testData = "test";
		ResponseEntity<ApiResponse<String>> response = ApiResponse.OK(testData);

		// act
		ApiResponse<String> apiResponse = response.getBody();

		// assert
		assertThat(apiResponse).isNotNull();
		assertThat(apiResponse.getHttpStatus()).isEqualTo(OK);
		assertThat(apiResponse.getHttpStatusCode()).isEqualTo(200);
		assertThat(apiResponse.getMessage()).isEqualTo(ApiResponse.DEFAULT_MESSAGE);
		assertThat(apiResponse.getData()).isEqualTo(testData);
	}

	// 테스트용 내부 클래스
	static class TestDto {
		private String name;
		private Integer value;

		public TestDto(String name, Integer value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}
	}
}
