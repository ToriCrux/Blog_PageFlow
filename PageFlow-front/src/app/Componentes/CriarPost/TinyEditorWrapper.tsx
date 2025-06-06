'use client';

import dynamic from 'next/dynamic';

const Editor = dynamic(() =>
  import('@tinymce/tinymce-react').then(mod => mod.Editor),
  { ssr: false }
);

// Tipagem simplificada sem dependência do módulo `tinymce`
interface TinyEditorWrapperProps {
  value: string;
  apiKey: string;
  onEditorChange: (content: string, editor: unknown) => void;
  init: Record<string, unknown>;
}

export default function TinyEditorWrapper(props: TinyEditorWrapperProps) {
  return <Editor {...props} />;
}
