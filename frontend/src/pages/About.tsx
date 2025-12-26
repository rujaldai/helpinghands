const About = () => {
  return (
    <div className="container mx-auto px-4 py-16">
      <div className="max-w-3xl mx-auto">
        <h1 className="text-4xl font-bold mb-8">About Helping Hands</h1>
        <div className="prose prose-lg">
          <p className="text-gray-700 mb-6">
            Helping Hands is a transparent donation platform that connects donors with causes
            and institutions that need support. We believe in complete transparency, allowing
            everyone to see how donations are being used.
          </p>
          <h2 className="text-2xl font-semibold mb-4">Our Mission</h2>
          <p className="text-gray-700 mb-6">
            To create a platform where giving is transparent, accessible, and impactful.
            We enable donors to support causes they care about while maintaining complete
            visibility into how their contributions are used.
          </p>
          <h2 className="text-2xl font-semibold mb-4">Transparency First</h2>
          <p className="text-gray-700 mb-6">
            Every donation on our platform is publicly viewable. We believe that transparency
            builds trust and encourages more giving. You can see who donated, how much, and
            what messages they left.
          </p>
        </div>
      </div>
    </div>
  );
};

export default About;

