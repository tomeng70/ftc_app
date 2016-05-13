import webapp2

class MainPage(webapp2.RequestHandler):
  def get(self):
    text = self.request.get('text', None)
    if text is not None:
      self.response.write(text)
      self.response.headers['Content-Type'] = 'text/plain'
      return
    self.redirect('/assets/FtcBlocks.html')

app = webapp2.WSGIApplication([
  ('/', MainPage),
], debug=True)
