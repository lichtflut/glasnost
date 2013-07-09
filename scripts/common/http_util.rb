
def init_http_object(uri)
  http  = Net::HTTP.new(uri.host, uri.port)
  if (uri.scheme == "https") 
    http.use_ssl     = true
    http.cert        = OpenSSL::X509::Certificate.new(pem)
    http.key         = OpenSSL::PKey::RSA.new(pem)
    http.verify_mode = OpenSSL::SSL::VERIFY_NONE
  end
  return http
end