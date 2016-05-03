package tds.tdsadmin.trclient;

import org.opentestsystem.shared.trapi.ITrClient;
import org.opentestsystem.shared.trapi.TrApiContentType;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class TestTrClient implements ITrClient {

	@Override
	public String getPackage(String url) {
		return null;
	}

	@Override
	public String getForObject(String url) {
		return null;
	}

	@Override
	public <T> T getForObject(String url, Class<T> responseType) {
		return null;
	}

	@Override
	public String getForObject(String url, TrApiContentType contentType) {
		return null;
	}

	@Override
	public <T> T getForObject(String url, TrApiContentType contentType, Class<T> responseType) {
		return null;
	}

	@Override
	public <T> T postForObject(String url, Object request, Class<T> responseType) {
		return null;
	}

	@Override
	public void put(String url, Object request) {
	}

	@Override
	public ResponseEntity<String> exchange(String url, String requestBody, TrApiContentType contentType,
			HttpMethod httpMethod) {
		return null;
	}

	@Override
	public <T> ResponseEntity<T> exchange(String url, String requestBody, TrApiContentType contentType,
			HttpMethod httpMethod, Class<T> responseType) {
		return null;
	}

}
