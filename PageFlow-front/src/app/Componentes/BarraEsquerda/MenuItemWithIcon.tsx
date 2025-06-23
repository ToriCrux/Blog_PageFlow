// BarraEsquerda/MenuItemWithIcon.tsx
"use client";

import { MenuItem, IconWrapper, IconLabel } from "./styles";

interface MenuItemWithIconProps {
  id:string;
  icon: string;
  label: string;
  selected?: boolean;
  expanded: boolean;
  onClick: () => void;
}

export function MenuItemWithIcon({
    id,
  icon,
  label,
  selected,
  expanded,
  onClick,
}: MenuItemWithIconProps) {
  return (
    <MenuItem id={id} $selected={selected} onClick={onClick}>
      <IconWrapper><i className={icon} /></IconWrapper>
      {expanded && <IconLabel>{label}</IconLabel>}
    </MenuItem>
  );
}
