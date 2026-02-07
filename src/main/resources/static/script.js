const submitBtn = document.getElementById("submit");
const output = document.getElementById("output");
const loading = document.getElementById("loading");

/**
 * Very small, dependency-free markdown parser
 * Covers: headings, bold, italics, lists, code blocks, line breaks
 * Not perfect. Good enough for LLM output.
 */
function markdownToHtml(md) {
  let html = md;

  // Escape HTML
  html = html.replace(/</g, "&lt;").replace(/>/g, "&gt;");

  // Code blocks ```
  html = html.replace(/```([\s\S]*?)```/g, "<pre><code>$1</code></pre>");

  // Headings ###
  html = html.replace(/^### (.*$)/gim, "<h3>$1</h3>");
  html = html.replace(/^## (.*$)/gim, "<h2>$1</h2>");
  html = html.replace(/^# (.*$)/gim, "<h1>$1</h1>");

  // Bold **text**
  html = html.replace(/\*\*(.*?)\*\*/g, "<strong>$1</strong>");

  // Italic *text*
  html = html.replace(/\*(.*?)\*/g, "<em>$1</em>");

  // Bullet lists
  html = html.replace(/^\s*-\s+(.*)/gim, "<li>$1</li>");
  html = html.replace(/(<li>.*<\/li>)/gims, "<ul>$1</ul>");

  // Line breaks
  html = html.replace(/\n\n+/g, "</p><p>");
  html = html.replace(/\n/g, "<br>");

  // Wrap in paragraph
  html = "<p>" + html + "</p>";

  return html;
}

submitBtn.onclick = async () => {
  const idea = document.getElementById("idea").value.trim();
  if (!idea) {
    alert("Startup idea is required.");
    return;
  }

  const payload = {
    idea,
    targetUsers: document.getElementById("users").value,
    monetization: document.getElementById("money").value,
    geography: document.getElementById("geo").value
  };

  output.innerHTML = "";
  loading.classList.remove("hidden");

  try {
    const res = await fetch("/api/reality-check", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });

    if (!res.ok) {
      const err = await res.text();
      throw new Error(err);
    }

    const data = await res.json();
    const markdown = data.output || JSON.stringify(data, null, 2);

    output.innerHTML = markdownToHtml(markdown);

  } catch (e) {
    output.innerHTML = `<pre>Error:\n${e.message}</pre>`;
  } finally {
    loading.classList.add("hidden");
  }
};
