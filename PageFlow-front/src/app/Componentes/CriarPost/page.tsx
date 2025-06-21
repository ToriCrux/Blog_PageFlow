"use client";

import { PostContainer, Divider, ActionsRow, ActionButton, SendButton, ToggleCheckbox } from "./styles";

import { useCriarPost } from "./useCriarPost";
import TinyEditorWrapper from "./TinyEditorWrapper";

import { poppins } from "../../fonts";

export default function CriarPost() {
  const {
    title,
    setTitle,
    post,
    setPost,
    categorias,
    selectedCategoria,
    setSelectedCategoria,
    isDraft,
    setIsDraft,
    handleSend,
  } = useCriarPost();

  return (
    <div className={poppins.className}>
      <PostContainer>
        <input
          type="text"
          placeholder="Title..."
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          className="w-full bg-transparent resize-none outline-none text-gray-700 placeholder-gray-500 mb-2"
        />

        <TinyEditorWrapper
          apiKey="4knoygedy11gmj5630ocpttktkw3zaeynfopcxk93zn86moc"
          value={post}
          onEditorChange={(content) => setPost(content)}
          init={{
            height: 300,
            menubar: false,
            plugins: [],
            toolbar:
              "undo redo | formatselect | bold italic underline | " +
              "alignleft aligncenter alignright alignjustify | " +
              "bullist numlist outdent indent | removeformat | help",
            content_style: "body { font-family:Helvetica,Arial,sans-serif; font-size:14px }",
          }}
        />

        <select
          aria-label="Selecionar categoria"
          value={selectedCategoria ?? ""}
          onChange={(e) => setSelectedCategoria(Number(e.target.value))}
          className="w-full mt-4 mb-2 p-2 rounded bg-white text-gray-800 border border-gray-300"
        >
          {categorias.map((cat) => (
            <option key={cat.id} value={cat.id}>
              {cat.name}
            </option>
          ))}
        </select>

        <Divider />

        <ActionsRow>
          <div className="flex gap-2">
            <ToggleCheckbox $checked={isDraft} onClick={() => setIsDraft(!isDraft)}>
              <i className="fa-solid fa-pencil mr-2"></i>
              {isDraft ? "Rascunho Ativo" : "Marcar como Rascunho"}
            </ToggleCheckbox>
            <ActionButton>
              <i className="fas fa-image mr-2" />
              Pictures
            </ActionButton>
            <ActionButton>
              <i className="fas fa-map-marker-alt mr-2" />
              Location
            </ActionButton>
          </div>

          <SendButton onClick={handleSend}>
            <i className="fas fa-paper-plane" />
          </SendButton>
        </ActionsRow>
      </PostContainer>
    </div>
  );
}
